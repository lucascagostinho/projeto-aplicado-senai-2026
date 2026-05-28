import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Adotante } from '../adotante.model';
import { AdotanteService } from '../adotante.service';

@Component({
  selector: 'app-adotante-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, ConfirmDialogModule,
            ToastModule, CardModule, InputTextModule, TooltipModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './adotante-list.html',
  styleUrl: './adotante-list.css'
})
export class AdotanteList implements OnInit {

  private _todos: Adotante[] = [];
  adotantes = signal<Adotante[]>([]);

  filtroNome  = '';
  filtroCpf   = '';
  filtroEmail = '';

  constructor(
    private service: AdotanteService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.service.listar().subscribe({
      next: (dados) => { this._todos = dados; this.adotantes.set(dados); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar adotantes.' })
    });
  }

  filtrar(): void {
    const nm = this.filtroNome.toLowerCase().trim();
    const cp = this.filtroCpf.toLowerCase().trim();
    const em = this.filtroEmail.toLowerCase().trim();
    this.adotantes.set(
      this._todos.filter(a =>
        (!nm || (a.nome ?? '').toLowerCase().includes(nm)) &&
        (!cp || (a.cpf ?? '').toLowerCase().includes(cp)) &&
        (!em || (a.email ?? '').toLowerCase().includes(em))
      )
    );
  }

  limparFiltros(): void {
    this.filtroNome  = '';
    this.filtroCpf   = '';
    this.filtroEmail = '';
    this.adotantes.set(this._todos);
  }

  get temFiltroAtivo(): boolean {
    return !!(this.filtroNome || this.filtroCpf || this.filtroEmail);
  }

  novo(): void { this.router.navigate(['/adotantes/novo']); }
  editar(a: Adotante): void { this.router.navigate(['/adotantes/editar', a.id]); }

  confirmarExclusao(a: Adotante): void {
    this.confirmationService.confirm({
      message: `Deseja excluir o adotante "${a.nome}"?`,
      header: 'Confirmar Exclusão', icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir', rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.excluir(a.id!)
    });
  }

  private excluir(id: number): void {
    this.service.deletar(id).subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Adotante excluído.' }); this.carregar(); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir adotante.' })
    });
  }
}
