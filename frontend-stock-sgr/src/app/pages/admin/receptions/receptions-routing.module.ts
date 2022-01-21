import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReceptionsComponent } from './receptions.component';

const routes: Routes = [{ path: '', component: ReceptionsComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReceptionsRoutingModule { }
