import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService } from 'primeng/api';
import { Solicitacao, SolicitacaoStatus } from '../solicitacao.model';
import { SolicitacaoService } from '../solicitacao.service';

@Component({
  selector: 'app-solicitacao-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, TagModule,
            DialogModule, ToastModule, CardModule, SelectModule, TextareaModule, TooltipModule],
  providers: [MessageService],
  templateUrl: './solicitacao-list.html',
  styleUrl: './solicitacao-list.css'
})
export class SolicitacaoList implements OnInit {

  private _todas: Solicitacao[] = [];
  solicitacoes = signal<Solicitacao[]>([]);

  filtroStatus: string | null = null;

  readonly statusOpcoes = [
    { label: 'Pendente',  value: 'PENDENTE'  },
    { label: 'Aprovada',  value: 'APROVADA'  },
    { label: 'Recusada',  value: 'RECUSADA'  },
    { label: 'Cancelada', value: 'CANCELADA' },
  ];

  dialogVisivel = false;
  dialogTitulo = '';
  dialogAcao: 'recusar' | 'cancelar' | null = null;
  dialogSolicitacaoId: number | null = null;
  justificativa = '';

  constructor(
    private service: SolicitacaoService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.service.listar().subscribe({
      next: (dados) => { this._todas = dados; this.solicitacoes.set(dados); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar solicitações.' })
    });
  }

  filtrar(): void {
    this.solicitacoes.set(
      this._todas.filter(s =>
        !this.filtroStatus || s.status === this.filtroStatus
      )
    );
  }

  limparFiltros(): void {
    this.filtroStatus = null;
    this.solicitacoes.set(this._todas);
  }

  get temFiltroAtivo(): boolean {
    return !!this.filtroStatus;
  }

  nova(): void { this.router.navigate(['/solicitacoes/nova']); }

  aprovar(id: number): void {
    this.service.aprovar(id).subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Aprovada', detail: 'Solicitação aprovada.' }); this.carregar(); },
      error: (err) => this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.detail ?? 'Erro ao aprovar.' })
    });
  }

  abrirDialog(id: number, acao: 'recusar' | 'cancelar'): void {
    this.dialogSolicitacaoId = id;
    this.dialogAcao = acao;
    this.dialogTitulo = acao === 'recusar' ? 'Recusar Solicitação' : 'Cancelar Solicitação';
    this.justificativa = '';
    this.dialogVisivel = true;
  }

  confirmarAcao(): void {
    if (!this.dialogSolicitacaoId || !this.dialogAcao) return;
    const req = this.dialogAcao === 'recusar'
      ? this.service.recusar(this.dialogSolicitacaoId, this.justificativa)
      : this.service.cancelar(this.dialogSolicitacaoId, this.justificativa);
    req.subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Ação realizada.' });
        this.dialogVisivel = false;
        this.carregar();
      },
      error: (err) => this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.detail ?? 'Erro ao executar ação.' })
    });
  }

  severidadeStatus(status: SolicitacaoStatus): 'success' | 'info' | 'warn' | 'danger' | 'secondary' {
    const map: Record<SolicitacaoStatus, 'success' | 'info' | 'warn' | 'danger' | 'secondary'> = {
      PENDENTE: 'warn', APROVADA: 'success', RECUSADA: 'danger', CANCELADA: 'secondary'
    };
    return map[status] ?? 'secondary';
  }

  labelStatus(status: SolicitacaoStatus): string {
    return this.statusOpcoes.find(o => o.value === status)?.label ?? status;
  }
}
