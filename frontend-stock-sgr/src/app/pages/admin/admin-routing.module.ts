import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';

const routes: Routes = [
  { path: '', component: AdminComponent,
  children:[
    {path:"",redirectTo:"acceuil",pathMatch:"full"},
    {path:"acceuil",loadChildren: () => import('./home/home.module').then(m => m.HomeModule)},
    { path: 'articles', loadChildren: () => import('./articles/articles.module').then(m => m.ArticlesModule) },
    { path: 'fournisseurs', loadChildren: () => import('./fournisseurs/fournisseurs.module').then(m => m.FournisseursModule) },
    { path: 'receptions', loadChildren: () => import('./receptions/receptions.module').then(m => m.ReceptionsModule) },
    { path: 'categories', loadChildren: () => import('./categories/categories.module').then(m => m.CategoriesModule) },
    { path: 'affectations', loadChildren: () => import('./affections/affections.module').then(m => m.AffectionsModule) },
    { path: 'employes', loadChildren: () => import('./employees/employees.module').then(m => m.EmployeesModule) },
    { path: 'division', loadChildren: () => import('./division/division.module').then(m => m.DivisionModule) },
    { path: 'marques', loadChildren: () => import('./marque/marque.module').then(m => m.MarqueModule) },
    { path: 'epaves', loadChildren: () => import('./epaves/epaves.module').then(m => m.EpavesModule) },
  ]
}

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
