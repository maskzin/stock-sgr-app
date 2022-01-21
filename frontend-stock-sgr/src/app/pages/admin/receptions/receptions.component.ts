import { ReceptionArticle } from './../../../shared/models/reception-article.model';
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
import { Affectation } from 'src/app/shared/models/affectation.model';
import { Categorie } from 'src/app/shared/models/categorie.model';
import { Reception } from 'src/app/shared/models/reception.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-receptions',
  templateUrl: './receptions.component.html',
  styleUrls: ['./receptions.component.scss']
})
export class ReceptionsComponent implements OnInit {

  Receptions :Reception[] = []
  Reception:Reception

  articles:Article[]=[]
  article:Article

  receptions:Reception[]=[]
  reception:Reception

  receptionArticles:ReceptionArticle[] = []
  receptionArticle:ReceptionArticle

  affectations:Affectation[]=[]
  affectation:Affectation

  fournisseurs:Fournisseur[]=[]
  fournisseur:Fournisseur

  categories:Categorie[]=[]
  categorie:Categorie

  employees:Employee[]=[]
  employee:Employee


  currentPage:string='list'
  loading:boolean=false
  ReceptionFilter: any = { libelleReception: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['id', 'libelle', 'categorie', 'stock','telephone','actions'];
  dataSource = new MatTableDataSource<Reception>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private ReceptionService:ReceptionService,
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private fournisseurService:FournisseurService,
    private employeeService:EmployeeService,
    private dialog:MatDialog,
    private toast:ToastrService,
    private articleService:ArticleService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.Receptions)
    this.findAll();
    this.findAllFournisseur();
    this.findAllAffectation();
    this.findAllCategorie();
    this.findAllArticles();
    this.findAllEmployees();
    this.searchParam = new SearchParam();
    this.reception = new Reception();
    this.receptionArticle = new ReceptionArticle()
  }



  applyFilter() {
    let filterValue = this.searchParam.query.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  private initDataTable(cercles: Reception[]) {
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
    this.receptionService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.receptions = ret["body"];
        this.initDataTable(this.receptions)
        this.loading = false
        this.toast.success(ret["body"].length+" Receptions ont été trouvés avec succès");
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

  findAllAffectation(){
    this.loading = true
    this.affectationService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectations = ret["body"];
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

  onSelection(data:Reception,text:string){
    this.reception = data;
    this.reception.receptionArticles = data.receptionArticles;
    console.log(this.reception)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Reception){
  this.reception = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "cette Receptions";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,
    dialogConfig
  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      // this.reception.articles.map(art=>art.isReception=false);
      this.findAllArticles();
      this.receptionService.delete(this.reception).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success("Reception a été supprimée avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.reception = new Reception();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.currentPage="add"
  this.reception = new Reception();
  this.receptionArticle = new ReceptionArticle()
}
onDeleteReceptionArticle(id:number){
    this.receptionArticles= this.receptionArticles.filter((recept,i)=>i!=id)
}

onAddReceptionArticle(){
  this.receptionArticles.push(this.receptionArticle)
  this.receptionArticle = new ReceptionArticle()
}

onSubmitReception(){
  // this.reception.articles = [this.article];
  this.loading = true
  this.reception.receptionArticles = this.receptionArticles;
  // this.reception.articles.map(art=>this.saveArticle(art));
  console.log(this.reception)
  this.receptionService.save(this.reception).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Reception sauvegardé avec succès");
        this.loading = false
        this.receptionArticles=[]
        console.log("Response")
        console.log(ret["body"])
        this.findAll();
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.reception = new Reception();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

saveArticle(article:Article){
  this.loading = true
  article.isReception = true;
  this.articleService.update(article).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Article sauvegardé avec succès");
        this.loading = false
    }else{
      this.loading=false
    }
    this.article = new Article();
  },(err)=>{
    this.loading=false
  })
}

onEditReception(){
  this.loading = true
  this.reception.receptionArticles = this.receptionArticles;

  // console.log(this.reception)
  this.receptionService.update(this.reception).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Reception enregistré avec succès");
      this.loading = false;
      this.findAll();
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.reception = new Reception();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

getArticleNonReception(articlist:Article[]){
  return articlist.filter(art=>art.isReception==false);
}

onClear(){this.reception = new Reception();this.currentPage = 'list'}

stockArticleCount(articles:Article[]){
  let sumStock = 0;
    articles.forEach(article => {
        sumStock += Number(article.stock);
    });
    return sumStock;
}

}
