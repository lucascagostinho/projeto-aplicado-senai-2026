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
import { OngService } from '../../usuario/ong/ong.service';
import { ProtetorService } from '../../usuario/protetor/protetor.service';
import { ESPECIES, SEXOS, FAIXAS_ETARIAS, PORTES, STATUS_OPCOES } from '../animal-options';

@Component({
  selector: 'app-animal-form',
  standalone: true,
  imports: [
    CommonModule, ReactiveFormsModule, CardModule, ButtonModule,
    InputTextModule, SelectModule, CheckboxModule, TextareaModule,
    MessageModule, ToastModule, DividerModule
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
  responsaveisOpcoes: { label: string; value: number | null }[] = [{ label: 'Sem responsável', value: null }];

  constructor(
    private fb: FormBuilder,
    private service: AnimalService,
    private ongService: OngService,
    private protetorService: ProtetorService,
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
      vacinado:        [false],
      responsavelId:   [null]
    });

    this.ongService.listar().subscribe({
      next: (ongs) => {
        const opcOng = ongs.map(o => ({ label: `[ONG] ${o.razaoSocial}`, value: o.id! }));
        this.protetorService.listar().subscribe({
          next: (protetores) => {
            const opcPro = protetores.map(p => ({ label: `[Protetor] ${p.nome}`, value: p.id! }));
            this.responsaveisOpcoes = [{ label: 'Sem responsável', value: null }, ...opcOng, ...opcPro];
          }
        });
      }
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
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.salvando.set(true);
    const dados = this.form.value;

    const req = (this.editando && this.animalId)
      ? this.service.atualizar(this.animalId, dados)
      : this.service.criar(dados);

    req.subscribe({
      next: () => this.router.navigate(['/animais']),
      error: () => {
        this.salvando.set(false);
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível salvar o animal.' });
      }
    });
  }

  cancelar(): void { this.router.navigate(['/animais']); }

  temErro(campo: string): boolean {
    const c = this.form.get(campo);
    return !!(c && c.invalid && c.touched);
  }
}
