import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { CheckboxModule } from 'primeng/checkbox';
import { TextareaModule } from 'primeng/textarea';
import { MessageModule } from 'primeng/message';
import { ToastModule } from 'primeng/toast';
import { DividerModule } from 'primeng/divider';
import { MessageService } from 'primeng/api';
import { AnimalService } from '../animal.service';
import { ESPECIES, SEXOS, FAIXAS_ETARIAS, PORTES, STATUS_OPCOES } from '../animal-options';

@Component({
  selector: 'app-animal-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CardModule,
    ButtonModule,
    InputTextModule,
    SelectModule,
    CheckboxModule,
    TextareaModule,
    MessageModule,
    ToastModule,
    DividerModule
  ],
  providers: [MessageService],
  templateUrl: './animal-form.html',
  styleUrl: './animal-form.css'
})
export class AnimalForm implements OnInit {

  form!: FormGroup;
  editando = false;
  animalId?: number;
  salvando = signal(false);

  readonly especies      = ESPECIES;
  readonly sexos         = SEXOS;
  readonly faixasEtarias = FAIXAS_ETARIAS;
  readonly portes        = PORTES;
  readonly statusOpcoes  = STATUS_OPCOES;

  constructor(
    private fb: FormBuilder,
    private service: AnimalService,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      especie:         [null, Validators.required],
      raca:            [''],
      sexo:            [null, Validators.required],
      faixaEtaria:     [null, Validators.required],
      porte:           [null, Validators.required],
      cor:             [''],
      caracteristicas: [''],
      status:          ['DISPONIVEL', Validators.required],
      foto:            [''],
      cidade:          ['', Validators.required],
      estado:          ['', [Validators.required, Validators.maxLength(2)]],
      castrado:        [false],
      vacinado:        [false]
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.editando = true;
      this.animalId = +id;
      this.service.buscarPorId(this.animalId).subscribe({
        next: (animal) => this.form.patchValue(animal),
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Animal não encontrado.' })
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.salvando.set(true);
    const dados = this.form.value;

    if (this.editando && this.animalId) {
      this.service.atualizar(this.animalId, dados).subscribe({
        next: () => this.router.navigate(['/animais']),
        error: () => {
          this.salvando.set(false);
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível atualizar o animal.' });
        }
      });
    } else {
      this.service.criar(dados).subscribe({
        next: () => this.router.navigate(['/animais']),
        error: () => {
          this.salvando.set(false);
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível cadastrar o animal.' });
        }
      });
    }
  }

  cancelar(): void {
    this.router.navigate(['/animais']);
  }

  temErro(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c && c.invalid && c.touched);
  }
}
