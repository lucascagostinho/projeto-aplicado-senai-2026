import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'adoption'
  },
  {
    path: 'adoption',
    loadChildren: () => import('./adoption/adoption-module').then(m => m.AdoptionModule)
  }
];
