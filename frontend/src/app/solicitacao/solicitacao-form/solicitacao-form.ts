import { Component, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { SelectModule } from 'primeng/select';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { SolicitacaoService } from '../solicitacao.service';
import { AnimalService } from '../../animal/animal.service';
import { AdotanteService } from '../../usuario/adotante/adotante.service';

@Component({
  selector: 'app-solicitacao-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CardModule, ButtonModule,
            SelectModule, TextareaModule, ToastModule],
  providers: [MessageService],
  templateUrl: './solicitacao-form.html',
  styleUrl: './solicitacao-form.css'
})
export class SolicitacaoForm implements OnInit {

  form!: FormGroup;
  salvando = signal(false);
  animaisOpcoes: { label: string; value: number }[] = [];
  adotantesOpcoes: { label: string; value: number }[] = [];

  constructor(
    private fb: FormBuilder,
    private service: SolicitacaoService,
    private animalService: AnimalService,
    private adotanteService: AdotanteService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      animalId:   [null, Validators.required],
      adotanteId: [null, Validators.required],
      mensagem:   ['']
    });

    this.animalService.listar().subscribe({
      next: (animais) => {
        this.animaisOpcoes = animais
          .filter(a => a.status === 'DISPONIVEL')
          .map(a => ({ label: `#${a.id} — ${a.especie === 'CAO' ? 'Cão' : 'Gato'}${a.raca ? ' ' + a.raca : ''}`, value: a.id! }));
      }
    });

    this.adotanteService.listar().subscribe({
      next: (adotantes) => {
        this.adotantesOpcoes = adotantes.map(a => ({ label: `#${a.id} — ${a.nome}`, value: a.id! }));
      }
    });
  }

  salvar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.salvando.set(true);
    this.service.criar(this.form.value).subscribe({
      next: () => this.router.navigate(['/solicitacoes']),
      error: (err) => {
        this.salvando.set(false);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.detail ?? 'Erro ao criar solicitação.' });
      }
    });
  }

  cancelar(): void { this.router.navigate(['/solicitacoes']); }
  temErro(campo: string): boolean { const c = this.form.get(campo); return !!(c?.invalid && c?.touched); }
}
