import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MarqueComponent } from './marque.component';

const routes: Routes = [{ path: '', component: MarqueComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MarqueRoutingModule { }
