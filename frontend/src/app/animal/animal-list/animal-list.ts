import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ToolbarModule } from 'primeng/toolbar';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Animal } from '../animal.model';
import { AnimalService } from '../animal.service';

@Component({
  selector: 'app-animal-list',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    TagModule,
    ToolbarModule,
    ConfirmDialogModule,
    ToastModule
  ],
  providers: [ConfirmationService, MessageService],
  templateUrl: './animal-list.html'
})
export class AnimalList implements OnInit {

  animais = signal<Animal[]>([]);

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
      disponivel: 'success',
      em_processo: 'warn',
      adotado: 'info',
      indisponivel: 'danger'
    };
    return map[status] ?? 'secondary';
  }

  labelStatus(status: string): string {
    const map: Record<string, string> = {
      disponivel: 'Disponível',
      em_processo: 'Em Processo',
      adotado: 'Adotado',
      indisponivel: 'Indisponível'
    };
    return map[status] ?? status;
  }

}
