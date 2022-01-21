import { AffectationArticle } from './../../../shared/models/affectation-article.model';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { Reception } from 'src/app/shared/models/reception.model';
import { EmployeeService } from './../../../shared/services/employee.service';
import { Employee } from 'src/app/shared/models/employee.model';
import { Fournisseur } from './../../../shared/models/fournisseur.model';
import { Article } from 'src/app/shared/models/article.model';

import { ArticleService } from 'src/app/shared/services/article.service';
import { FournisseurService } from 'src/app/shared/services/fournisseur.service';
import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Categorie } from 'src/app/shared/models/categorie.model';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-affections',
  templateUrl: './affections.component.html',
  styleUrls: ['./affections.component.scss']
})
export class AffectionsComponent implements OnInit {


  Affectations :Affectation[]=[]
  Affectation:Affectation

  articles:Article[]=[]
  article:Article

 receptions:Reception[]=[]
 reception:Reception

  affectations:Affectation[]=[]
  affectation:Affectation

  affectationArticle:AffectationArticle
  affectationArticles:AffectationArticle[] = []

  fournisseurs:Fournisseur[]=[]
  fournisseur:Fournisseur

  categories:Categorie[]=[]
  categorie:Categorie

  employees:Employee[]=[]
  employee:Employee


  currentPage:string="list"
  loading:boolean=false
  AffectationFilter: any = { libelleAffectation: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['id', 'libelle', 'categorie', 'stock','telephone','actions'];
  dataSource = new MatTableDataSource<Affectation>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private fournisseurService:FournisseurService,
    private employeeService:EmployeeService,
    private dialog:MatDialog,
    private toast:ToastrService,
    private articleService:ArticleService,
    private receptionService:ReceptionService,
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.affectations)
    this.findAll();
    this.findAllFournisseur();
    this.findAllRecption();
    this.findAllCategorie();
    this.findAllArticles();
    this.findAllEmployees();
    this.searchParam = new SearchParam();
  }



  applyFilter() {
    let filterValue = this.searchParam.query.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  private initDataTable(cercles: Affectation[]) {
    this.dataSource = new MatTableDataSource(cercles);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAllArticles(){
    this.loading = true
    this.articleService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.articles = ret["body"];
        this.loading = false
        console.log(this.articles)
      }else{
        this.toast.error(environment.error_message);
        this.loading=false
      }

    },err=>{
      this.toast.error(environment.error_message);
      this.loading=false
    })
  }
  findAllEmployees(){
    this.loading = true
    this.employeeService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.employees = ret["body"];
        this.loading = false
      }else{
        this.toast.error(environment.error_message);
        this.loading=false
      }

    },err=>{
      this.toast.error(environment.error_message);
      this.loading=false
    })
  }

  findAll(){
    this.loading = true
    this.affectationService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectations = ret["body"];
        this.initDataTable(this.affectations)
        console.log(ret["body"])
        this.loading = false
        this.toast.success(ret["body"].length+" Affectations ont été trouvés avec succès");
      }else{
        this.toast.error(environment.error_message);
        this.loading=false
      }

    },err=>{
      this.toast.error(environment.error_message);
      this.loading=false
    })
  }
  findAllFournisseur(){
    this.loading = true
    this.fournisseurService.findAll().subscribe(
      ret=>{
        if (ret["status"]===201 || ret["status"]===200) {
          this.fournisseurs = ret["body"];
          this.loading = false
        }else{
          console.log(environment.error_message)
        this.loading=false
        }
    },err=>{
      console.log(environment.error_message)
      this.loading=false
    })
  }

  findAllRecption(){
    this.loading = true
    this.receptionService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.receptions = ret["body"];
        this.loading = false
      }else{
        console.log(environment.error_message)
      this.loading=false
      }
    },err=>{
      console.log(environment.error_message)
      this.loading=false
    })
  }

  findAllCategorie(){
    this.loading = true
    this.categorieService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.categories = ret["body"];
        this.loading = false
      }else{
        console.log(environment.error_message)
      this.loading=false
      }
    },(err)=>{
      console.log(environment.error_message)
      this.loading=false
    })
  }

  onSelection(data:Affectation,text:string){
    this.affectation = data;
    console.log(this.affectation)
    this.currentPage = text;
}


updateArticle(article:Article){
  this.loading = true
  article.isSelected = false;
  this.articleService.update(article).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.loading = false
    }else{
      this.loading=false
    }
    this.article = new Article();
  },(err)=>{
    this.loading=false
  })
}



onListSelection(){this.currentPage='list'}

onDelete(data:Affectation){
  this.affectation = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "cette Affecattion";
  let dialogRef = this.dialog.open(DeleteArticleComponent,dialogConfig);
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      if (this.affectation.articles!==null) {
        this.affectation.articles.map(art=>this.updateArticle(art));
      }
      this.findAllArticles();
      this.affectationService.delete(this.affectation).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success("Affectation a été supprimée avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.affectation = new Affectation();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
getArticleNonAttribue(articlist:Article[]){
  return articlist.filter(art=>art.isSelected==false);
}
onAdd(){
  this.affectation = new Affectation();
  this.affectationArticle = new AffectationArticle()
  this.currentPage="add"
}

saveArticle(article:Article){
  this.loading = true
  article.isSelected = true;
  this.articleService.update(article).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.loading = false
    }else{
      this.loading=false
    }
    this.article = new Article();
  },(err)=>{
    this.loading=false
  })
}


onDeleteReceptionArticle(id:number){
  this.affectationArticles= this.affectationArticles.filter((recept,i)=>i!=id)
}

onAddReceptionArticle(){
this.affectationArticles.push(this.affectationArticle)
this.affectationArticle = new AffectationArticle()
}


onSubmitAffectation(){
  console.log("Add")
  console.log(this.affectation)
  this.affectation.affectationArticles = this.affectationArticles
  this.loading = true
  this.affectationService.save(this.affectation).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Affectation sauvegardé avec succès");
        this.loading = false
        this.findAll();
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.affectation = new Affectation();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditAffectation(){
  console.log("Edit")
  console.log(this.affectation)
  this.affectation.affectationArticles = this.affectationArticles
  this.loading = true
  this.affectationService.update(this.affectation).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Affectation enregistré avec succès");
      this.loading = false;
      this.findAll();
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.affectation = new Affectation();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.affectation = new Affectation();this.currentPage = 'list'}

}
