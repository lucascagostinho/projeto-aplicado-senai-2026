import { Routes } from '@angular/router';
import { AnimalList } from './animal/animal-list/animal-list';
import { AnimalForm } from './animal/animal-form/animal-form';
import { OngList } from './usuario/ong/ong-list/ong-list';
import { OngForm } from './usuario/ong/ong-form/ong-form';
import { ProtetorList } from './usuario/protetor/protetor-list/protetor-list';
import { ProtetorForm } from './usuario/protetor/protetor-form/protetor-form';
import { AdotanteList } from './usuario/adotante/adotante-list/adotante-list';
import { AdotanteForm } from './usuario/adotante/adotante-form/adotante-form';
import { SolicitacaoList } from './solicitacao/solicitacao-list/solicitacao-list';
import { SolicitacaoForm } from './solicitacao/solicitacao-form/solicitacao-form';
import { AdocaoList } from './adocao/adocao-list/adocao-list';
import { AdocaoForm } from './adocao/adocao-form/adocao-form';

export const routes: Routes = [
  { path: '', redirectTo: 'animais', pathMatch: 'full' },

  { path: 'animais',             component: AnimalList },
  { path: 'animais/novo',        component: AnimalForm },
  { path: 'animais/editar/:id',  component: AnimalForm },

  { path: 'ongs',                component: OngList },
  { path: 'ongs/nova',           component: OngForm },
  { path: 'ongs/editar/:id',     component: OngForm },

  { path: 'protetores',           component: ProtetorList },
  { path: 'protetores/novo',      component: ProtetorForm },
  { path: 'protetores/editar/:id', component: ProtetorForm },

  { path: 'adotantes',            component: AdotanteList },
  { path: 'adotantes/novo',       component: AdotanteForm },
  { path: 'adotantes/editar/:id', component: AdotanteForm },

  { path: 'solicitacoes',         component: SolicitacaoList },
  { path: 'solicitacoes/nova',    component: SolicitacaoForm },

  { path: 'adocoes',              component: AdocaoList },
  { path: 'adocoes/nova',         component: AdocaoForm }
];
