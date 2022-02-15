import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticlesComponent } from './articles.component';

const routes: Routes = [{
  path: '',
  component: ArticlesComponent ,
  children:[
    { path: 'detail', component: ArticlesComponent },
    { path: 'detail/:id', component: ArticlesComponent }
  ]
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ArticlesRoutingModule { }
