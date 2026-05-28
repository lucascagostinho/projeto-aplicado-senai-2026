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
import { Ong } from '../ong.model';
import { OngService } from '../ong.service';

@Component({
  selector: 'app-ong-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, ConfirmDialogModule,
            ToastModule, CardModule, InputTextModule, TooltipModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './ong-list.html',
  styleUrl: './ong-list.css'
})
export class OngList implements OnInit {

  private _todos: Ong[] = [];
  ongs = signal<Ong[]>([]);

  filtroRazaoSocial = '';
  filtroCnpj        = '';
  filtroEmail       = '';

  constructor(
    private service: OngService,
    private router: Router,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.service.listar().subscribe({
      next: (dados) => { this._todos = dados; this.ongs.set(dados); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar as ONGs.' })
    });
  }

  filtrar(): void {
    const rs = this.filtroRazaoSocial.toLowerCase().trim();
    const cn = this.filtroCnpj.toLowerCase().trim();
    const em = this.filtroEmail.toLowerCase().trim();
    this.ongs.set(
      this._todos.filter(o =>
        (!rs || (o.razaoSocial ?? '').toLowerCase().includes(rs)) &&
        (!cn || (o.cnpj ?? '').toLowerCase().includes(cn)) &&
        (!em || (o.email ?? '').toLowerCase().includes(em))
      )
    );
  }

  limparFiltros(): void {
    this.filtroRazaoSocial = '';
    this.filtroCnpj        = '';
    this.filtroEmail       = '';
    this.ongs.set(this._todos);
  }

  get temFiltroAtivo(): boolean {
    return !!(this.filtroRazaoSocial || this.filtroCnpj || this.filtroEmail);
  }

  nova(): void { this.router.navigate(['/ongs/nova']); }
  editar(ong: Ong): void { this.router.navigate(['/ongs/editar', ong.id]); }

  confirmarExclusao(ong: Ong): void {
    this.confirmationService.confirm({
      message: `Deseja excluir a ONG "${ong.razaoSocial}"?`,
      header: 'Confirmar Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Excluir', rejectLabel: 'Cancelar',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.excluir(ong.id!)
    });
  }

  private excluir(id: number): void {
    this.service.deletar(id).subscribe({
      next: () => { this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'ONG excluída.' }); this.carregar(); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível excluir a ONG.' })
    });
  }
}
