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
import { AdotanteService } from '../adotante.service';

@Component({
  selector: 'app-adotante-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CardModule, ButtonModule,
            InputTextModule, ToastModule, DividerModule],
  providers: [MessageService],
  templateUrl: './adotante-form.html',
  styleUrl: './adotante-form.css'
})
export class AdotanteForm implements OnInit {

  form!: FormGroup;
  editando = false;
  adotanteId?: number;
  salvando = signal(false);

  constructor(
    private fb: FormBuilder,
    private service: AdotanteService,
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
      this.adotanteId = +id;
      this.service.buscarPorId(this.adotanteId).subscribe({
        next: (a) => this.form.patchValue(a),
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Adotante não encontrado.' })
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.salvando.set(true);
    const req = this.editando
      ? this.service.atualizar(this.adotanteId!, this.form.value)
      : this.service.criar(this.form.value);

    req.subscribe({
      next: () => this.router.navigate(['/adotantes']),
      error: () => { this.salvando.set(false); this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao salvar adotante.' }); }
    });
  }

  cancelar(): void { this.router.navigate(['/adotantes']); }
  temErro(campo: string): boolean { const c = this.form.get(campo); return !!(c?.invalid && c?.touched); }
}
