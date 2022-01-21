import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { Article } from 'src/app/shared/models/article.model';
import { Categorie } from 'src/app/shared/models/categorie.model';
import { Reception } from 'src/app/shared/models/reception.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { ArticleService } from 'src/app/shared/services/article.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {

  articles :Article[]
  article:Article

  receptions:Reception[]
  reception:Reception

  affectations:Affectation[]
  affectation:Affectation

  categories:Categorie[]
  categorie:Categorie
  currentPage:string="list"
  loading:boolean=false
  articleFilter: any = { libelleArticle: '' };
  searchParam: SearchParam
  where:string="Article"

  displayedColumns: string[] = ['id', 'libelle', 'categorie', 'stock','actions'];
  dataSource = new MatTableDataSource<Article>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private articleService:ArticleService,
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private dialog:MatDialog,
    private toast:ToastrService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.articles)
    this.findAll();
    this.findAllReception();
    this.findAllAffectation();
    this.findAllCategorie();
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

  private initDataTable(cercles: Article[]) {
    this.dataSource = new MatTableDataSource(cercles);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAll(){
    this.loading = true
    this.articleService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.articles = ret["body"];
        this.initDataTable(this.articles)
        this.loading = false
        this.toast.success(ret["body"].length+" Articles ont été trouvés avec succès");
      }else{
        this.toast.error(environment.error_message);
        this.loading=false
      }

    },err=>{
      this.toast.error(environment.error_message);
      this.loading=false
    })
  }
  findAllReception(){
    this.loading = true
    this.receptionService.findAll().subscribe(
      ret=>{
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

  onSelection(data:Article,text:string){
    this.article = data;
    console.log(this.article)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Article){
  this.article = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "cet Article";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,dialogConfig

  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      this.articleService.delete(this.article).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success(this.article.libelleArticle+" a été supprimé avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.article = new Article();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.article = new Article();
  this.currentPage="add"
}
onSubmitArticle(){
  this.loading = true
  this.articleService.save(this.article).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Article sauvegardé avec succès");
        this.loading = false
        this.findAll();
        this.onListSelection();
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.article = new Article();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditArticle(){
  this.loading = true
  this.articleService.update(this.article).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Article enregistré avec succès");
      this.loading = false;
      this.findAll();
      this.onListSelection();
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.article = new Article();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.article = new Article();this.currentPage = 'list'}

}
