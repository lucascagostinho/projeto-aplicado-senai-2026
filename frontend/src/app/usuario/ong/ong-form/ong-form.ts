import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { MessageService } from 'primeng/api';
import { OngService } from '../ong.service';

@Component({
  selector: 'app-ong-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CardModule, ButtonModule,
            InputTextModule, TextareaModule, ToastModule, DividerModule],
  providers: [MessageService],
  templateUrl: './ong-form.html',
  styleUrl: './ong-form.css'
})
export class OngForm implements OnInit {

  form!: FormGroup;
  editando = false;
  ongId?: number;
  salvando = signal(false);

  constructor(
    private fb: FormBuilder,
    private service: OngService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      razaoSocial: ['', Validators.required],
      cnpj:        [''],
      email:       ['', [Validators.required, Validators.email]],
      senha:       ['', Validators.required],
      telefone:    [''],
      cep:         [''],
      descricao:   ['']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.editando = true;
      this.ongId = +id;
      this.service.buscarPorId(this.ongId).subscribe({
        next: (ong) => this.form.patchValue(ong),
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'ONG não encontrada.' })
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.salvando.set(true);
    const dados = this.form.value;
    const req = this.editando
      ? this.service.atualizar(this.ongId!, dados)
      : this.service.criar(dados);

    req.subscribe({
      next: () => this.router.navigate(['/ongs']),
      error: () => {
        this.salvando.set(false);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao salvar ONG.' });
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/ongs']);
  }

  temErro(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c?.invalid && c?.touched);
  }
}
