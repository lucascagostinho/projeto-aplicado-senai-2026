import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Animal, AnimalFiltro } from '../animal.model';
import { AnimalService } from '../animal.service';
import { ESPECIES, SEXOS, FAIXAS_ETARIAS, PORTES, STATUS_OPCOES } from '../animal-options';

@Component({
  selector: 'app-animal-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
    TagModule,
    ToolbarModule,
    ConfirmDialogModule,
    ToastModule,
    SelectModule,
    InputTextModule,
    CardModule
  ],
  providers: [ConfirmationService, MessageService],
  templateUrl: './animal-list.html',
  styleUrl: './animal-list.css'
})
export class AnimalList implements OnInit {

  animais = signal<Animal[]>([]);

  filtroEspecie:     string | null = null;
  filtroPorte:       string | null = null;
  filtroFaixaEtaria: string | null = null;
  filtroSexo:        string | null = null;
  filtroCidade = '';
  filtroStatus:      string | null = null;

  readonly especies      = ESPECIES;
  readonly portes        = PORTES;
  readonly faixasEtarias = FAIXAS_ETARIAS;
  readonly sexos         = SEXOS;
  readonly statusOpcoes  = STATUS_OPCOES;

  constructor(
    private service: AnimalService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.service.listar().subscribe({
      next: (dados) => this.animais.set(dados),
      error: () => this.messageService.add({
        severity: 'error',
        summary: 'Erro',
        detail: 'Não foi possível carregar os animais. Verifique se o backend está rodando.'
      })
    });
  }

  filtrar(): void {
    const filtro: AnimalFiltro = {
      especie:     this.filtroEspecie,
      porte:       this.filtroPorte,
      faixaEtaria: this.filtroFaixaEtaria,
      sexo:        this.filtroSexo,
      cidade:      this.filtroCidade,
      status:      this.filtroStatus,
    };
    this.service.listar(filtro).subscribe({
      next: (dados) => this.animais.set(dados),
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao filtrar animais.' })
    });
  }

  limparFiltros(): void {
    this.filtroEspecie = this.filtroPorte = this.filtroFaixaEtaria =
    this.filtroSexo = this.filtroStatus = null;
    this.filtroCidade = '';
    this.carregar();
  }

  get temFiltroAtivo(): boolean {
    return !!(this.filtroEspecie || this.filtroPorte || this.filtroFaixaEtaria ||
              this.filtroSexo || this.filtroStatus || this.filtroCidade.trim());
  }

  novo(): void {
    this.router.navigate(['/animais/novo']);
  }

  editar(animal: Animal): void {
    this.router.navigate(['/animais/editar', animal.id]);
  }

  confirmarExclusao(animal: Animal): void {
    this.confirmationService.confirm({
      message: `Deseja excluir o animal #${animal.id} (${animal.especie} — ${animal.raca || 'sem raça'})?`,
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir',
      rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.excluir(animal.id!)
    });
  }

  private excluir(id: number): void {
    this.service.deletar(id).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Animal excluído.' });
        this.carregar();
      },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível excluir o animal.' })
    });
  }

  severityStatus(status: string): 'success' | 'info' | 'warn' | 'danger' | 'secondary' {
    const map: Record<string, 'success' | 'info' | 'warn' | 'danger' | 'secondary'> = {
      DISPONIVEL:   'success',
      EM_PROCESSO:  'warn',
      ADOTADO:      'info',
      INDISPONIVEL: 'danger'
    };
    return map[status] ?? 'secondary';
  }

  labelStatus(status: string): string {
    return this.statusOpcoes.find(o => o.value === status)?.label ?? status;
  }

  labelEspecie(v: string): string {
    return this.especies.find(o => o.value === v)?.label ?? v;
  }

  labelSexo(v: string): string {
    return this.sexos.find(o => o.value === v)?.label ?? v;
  }

  labelFaixaEtaria(v: string): string {
    return this.faixasEtarias.find(o => o.value === v)?.label ?? v;
  }

  labelPorte(v: string): string {
    return this.portes.find(o => o.value === v)?.label ?? v;
  }
}
