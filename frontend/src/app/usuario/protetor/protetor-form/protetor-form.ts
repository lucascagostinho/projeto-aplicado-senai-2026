import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { MessageService } from 'primeng/api';
import { ProtetorService } from '../protetor.service';

@Component({
  selector: 'app-protetor-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CardModule, ButtonModule,
            InputTextModule, ToastModule, DividerModule],
  providers: [MessageService],
  templateUrl: './protetor-form.html',
  styleUrl: './protetor-form.css'
})
export class ProtetorForm implements OnInit {

  form!: FormGroup;
  editando = false;
  protetorId?: number;
  salvando = signal(false);

  constructor(
    private fb: FormBuilder,
    private service: ProtetorService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nome:     ['', Validators.required],
      cpf:      [''],
      email:    ['', [Validators.required, Validators.email]],
      senha:    ['', Validators.required],
      telefone: [''],
      cep:      ['']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.editando = true;
      this.protetorId = +id;
      this.service.buscarPorId(this.protetorId).subscribe({
        next: (p) => this.form.patchValue(p),
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Protetor não encontrado.' })
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.salvando.set(true);
    const req = this.editando
      ? this.service.atualizar(this.protetorId!, this.form.value)
      : this.service.criar(this.form.value);

    req.subscribe({
      next: () => this.router.navigate(['/protetores']),
      error: () => { this.salvando.set(false); this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao salvar protetor.' }); }
    });
  }

  cancelar(): void { this.router.navigate(['/protetores']); }
  temErro(campo: string): boolean { const c = this.form.get(campo); return !!(c?.invalid && c?.touched); }
}
