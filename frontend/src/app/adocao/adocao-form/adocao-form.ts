import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { DatePickerModule } from 'primeng/datepicker';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { AdocaoService } from '../adocao.service';
import { SolicitacaoService } from '../../solicitacao/solicitacao.service';
import { OngService } from '../../usuario/ong/ong.service';
import { ProtetorService } from '../../usuario/protetor/protetor.service';

@Component({
  selector: 'app-adocao-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CardModule, ButtonModule,
            SelectModule, DatePickerModule, ToastModule],
  providers: [MessageService],
  templateUrl: './adocao-form.html',
  styleUrl: './adocao-form.css'
})
export class AdocaoForm implements OnInit {

  form!: FormGroup;
  salvando = signal(false);
  solicitacoesOpcoes: { label: string; value: number }[] = [];
  responsaveisOpcoes: { label: string; value: number }[] = [];

  constructor(
    private fb: FormBuilder,
    private service: AdocaoService,
    private solicitacaoService: SolicitacaoService,
    private ongService: OngService,
    private protetorService: ProtetorService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      solicitacaoId:   [null, Validators.required],
      confirmadoPorId: [null, Validators.required],
      dataAdocao:      [null, Validators.required]
    });

    this.solicitacaoService.listar().subscribe({
      next: (lista) => {
        this.solicitacoesOpcoes = lista
          .filter(s => s.status === 'APROVADA')
          .map(s => ({
            label: `#${s.id} — ${s.animalEspecie === 'CAO' ? 'Cão' : 'Gato'}${s.animalRaca ? ' ' + s.animalRaca : ''} → ${s.adotanteNome || 'Adotante'}`,
            value: s.id!
          }));
      }
    });

    this.ongService.listar().subscribe({
      next: (ongs) => {
        const opcOng = ongs.map(o => ({ label: `[ONG] ${o.razaoSocial}`, value: o.id! }));
        this.protetorService.listar().subscribe({
          next: (protetores) => {
            const opcPro = protetores.map(p => ({ label: `[Protetor] ${p.nome}`, value: p.id! }));
            this.responsaveisOpcoes = [...opcOng, ...opcPro];
          }
        });
      }
    });
  }

  salvar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.salvando.set(true);
    const v = this.form.value;
    const dataFormatada = v.dataAdocao instanceof Date
      ? v.dataAdocao.toISOString().split('T')[0]
      : v.dataAdocao;

    this.service.confirmar({ ...v, dataAdocao: dataFormatada }).subscribe({
      next: () => this.router.navigate(['/adocoes']),
      error: (err) => {
        this.salvando.set(false);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.detail ?? 'Erro ao confirmar adoção.' });
      }
    });
  }

  cancelar(): void { this.router.navigate(['/adocoes']); }
  temErro(campo: string): boolean { const c = this.form.get(campo); return !!(c?.invalid && c?.touched); }
}
