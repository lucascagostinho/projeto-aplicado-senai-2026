import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Adoption } from './adoption/adoption';

const routes: Routes = [
  {
    path: '',
    component: Adoption
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdoptionRoutingModule {}
