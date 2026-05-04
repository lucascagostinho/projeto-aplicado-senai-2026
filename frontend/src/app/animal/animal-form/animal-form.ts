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
  templateUrl: './animal-form.html'
})
export class AnimalForm implements OnInit {

  form!: FormGroup;
  editando = false;
  animalId?: number;
  salvando = signal(false);

  readonly especies = [
    { label: 'Cão', value: 'CAO' },
    { label: 'Gato', value: 'GATO' }
  ];
  readonly sexos = [
    { label: 'Macho', value: 'MACHO' },
    { label: 'Fêmea', value: 'FEMEA' }
  ];
  readonly faixasEtarias = [
    { label: 'Filhote', value: 'FILHOTE' },
    { label: 'Jovem', value: 'JOVEM' },
    { label: 'Adulto', value: 'ADULTO' },
    { label: 'Sênior', value: 'SENIOR' }
  ];
  readonly portes = [
    { label: 'Pequeno', value: 'PEQUENO' },
    { label: 'Médio', value: 'MEDIO' },
    { label: 'Grande', value: 'GRANDE' }
  ];
  readonly statusOpcoes = [
    { label: 'Disponível', value: 'DISPONIVEL' },
    { label: 'Em Processo', value: 'EM_PROCESSO' },
    { label: 'Adotado', value: 'ADOTADO' },
    { label: 'Indisponível', value: 'INDISPONIVEL' }
  ];

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
      status:          [{ label: 'Disponível', value: 'DISPONIVEL' }, Validators.required],
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
        next: (animal) => {
          this.form.patchValue({
            ...animal,
            especie:     this.especies.find(e => e.value === animal.especie) ?? null,
            sexo:        this.sexos.find(s => s.value === animal.sexo) ?? null,
            faixaEtaria: this.faixasEtarias.find(f => f.value === animal.faixaEtaria) ?? null,
            porte:       this.portes.find(p => p.value === animal.porte) ?? null,
            status:      this.statusOpcoes.find(s => s.value === animal.status) ?? null
          });
        },
        error: () => this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Animal não encontrado.' })
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.value;
    const dados = {
      ...raw,
      especie:     raw.especie?.value ?? raw.especie,
      sexo:        raw.sexo?.value ?? raw.sexo,
      faixaEtaria: raw.faixaEtaria?.value ?? raw.faixaEtaria,
      porte:       raw.porte?.value ?? raw.porte,
      status:      raw.status?.value ?? raw.status
    };

    this.salvando.set(true);

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
