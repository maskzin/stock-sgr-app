import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AffectionsComponent } from './affections.component';

const routes: Routes = [{ path: '', component: AffectionsComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AffectionsRoutingModule { }
