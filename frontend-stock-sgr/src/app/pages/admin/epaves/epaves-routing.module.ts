import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EpavesComponent } from './epaves.component';

const routes: Routes = [{ path: '', component: EpavesComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EpavesRoutingModule { }
