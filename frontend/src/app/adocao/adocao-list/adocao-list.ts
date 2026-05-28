import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { DatePickerModule } from 'primeng/datepicker';
import { MessageService } from 'primeng/api';
import { Adocao } from '../adocao.model';
import { AdocaoService } from '../adocao.service';

@Component({
  selector: 'app-adocao-list',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ButtonModule, ToastModule,
            CardModule, InputTextModule, DatePickerModule],
  providers: [MessageService],
  templateUrl: './adocao-list.html',
  styleUrl: './adocao-list.css'
})
export class AdocaoList implements OnInit {

  private _todas: Adocao[] = [];
  adocoes = signal<Adocao[]>([]);

  filtroDataInicio: Date | null = null;
  filtroDataFim:    Date | null = null;
  filtroResponsavel = '';

  constructor(
    private service: AdocaoService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.service.listar().subscribe({
      next: (dados) => { this._todas = dados; this.adocoes.set(dados); },
      error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar adoções.' })
    });
  }

  filtrar(): void {
    const resp = this.filtroResponsavel.toLowerCase().trim();
    this.adocoes.set(
      this._todas.filter(a => {
        const data = new Date(a.dataAdocao);
        if (this.filtroDataInicio && data < this.filtroDataInicio) return false;
        if (this.filtroDataFim) {
          const fim = new Date(this.filtroDataFim);
          fim.setHours(23, 59, 59);
          if (data > fim) return false;
        }
        if (resp && !(a.confirmadoPorNome ?? '').toLowerCase().includes(resp)) return false;
        return true;
      })
    );
  }

  limparFiltros(): void {
    this.filtroDataInicio = null;
    this.filtroDataFim    = null;
    this.filtroResponsavel = '';
    this.adocoes.set(this._todas);
  }

  get temFiltroAtivo(): boolean {
    return !!(this.filtroDataInicio || this.filtroDataFim || this.filtroResponsavel);
  }

  nova(): void { this.router.navigate(['/adocoes/nova']); }
}
