import { Routes } from '@angular/router';
import { AnimalList } from './animal/animal-list/animal-list';
import { AnimalForm } from './animal/animal-form/animal-form';

export const routes: Routes = [
  { path: '', redirectTo: 'animais', pathMatch: 'full' },
  { path: 'animais', component: AnimalList },
  { path: 'animais/novo', component: AnimalForm },
  { path: 'animais/editar/:id', component: AnimalForm }
];
