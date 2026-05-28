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
import { Protetor } from '../protetor.model';
import { ProtetorService } from '../protetor.service';

@Component({
  selector: 'app-protetor-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, ConfirmDialogModule,
            ToastModule, CardModule, InputTextModule, TooltipModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './protetor-list.html',
  styleUrl: './protetor-list.css'
})
export class ProtetorList implements OnInit {

  private _todos: Protetor[] = [];
  protetores = signal<Protetor[]>([]);

  filtroNome  = '';
  filtroCpf   = '';
  filtroEmail = '';

  constructor(
    private service: ProtetorService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.service.listar().subscribe({
      next: (dados) => { this._todos = dados; this.protetores.set(dados); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar protetores.' })
    });
  }

  filtrar(): void {
    const nm = this.filtroNome.toLowerCase().trim();
    const cp = this.filtroCpf.toLowerCase().trim();
    const em = this.filtroEmail.toLowerCase().trim();
    this.protetores.set(
      this._todos.filter(p =>
        (!nm || (p.nome ?? '').toLowerCase().includes(nm)) &&
        (!cp || (p.cpf ?? '').toLowerCase().includes(cp)) &&
        (!em || (p.email ?? '').toLowerCase().includes(em))
      )
    );
  }

  limparFiltros(): void {
    this.filtroNome  = '';
    this.filtroCpf   = '';
    this.filtroEmail = '';
    this.protetores.set(this._todos);
  }

  get temFiltroAtivo(): boolean {
    return !!(this.filtroNome || this.filtroCpf || this.filtroEmail);
  }

  novo(): void { this.router.navigate(['/protetores/novo']); }
  editar(p: Protetor): void { this.router.navigate(['/protetores/editar', p.id]); }

  confirmarExclusao(p: Protetor): void {
    this.confirmationService.confirm({
      message: `Deseja excluir o protetor "${p.nome}"?`,
      header: 'Confirmar Exclusão', icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir', rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.excluir(p.id!)
    });
  }

  private excluir(id: number): void {
    this.service.deletar(id).subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Protetor excluído.' }); this.carregar(); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao excluir protetor.' })
    });
  }
}
