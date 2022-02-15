import { AffectationArticle } from './../../../shared/models/affectation-article.model';
import { ReceptionArticle } from './../../../shared/models/reception-article.model';
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
import { Router } from '@angular/router';
import { Marque } from 'src/app/shared/models/marque.model';
import { MarqueService } from 'src/app/shared/services/marque.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.scss']
})
export class ArticlesComponent implements OnInit {

  articles :Article[] = []
  article:Article

  receptions:Reception[] = []
  reception:Reception

  affectations:Affectation[]= []
  affectation:Affectation
  receptionArticles:ReceptionArticle[] = []
  receptionArticle :ReceptionArticle;

  affectationsArticles:AffectationArticle[] = []
  affectationsArticle : AffectationArticle;
  articleList:Article[] = [];

  categories:Categorie[]= []
  categorie:Categorie
  currentPage:String="list"
  loading:boolean=false
  catFilter: any = { nom: '' };
  marqueFilter:any= {libelle:''}
  searchParam: SearchParam
  where:string="Article"
  quantiteReceptionArticle:number
  quantiteAffectationArticle:number
  id:number

  displayedColumns: string[] = ['libelle', 'categorie', 'stock','qteRestant','marque','etat','actions'];
  dataSource = new MatTableDataSource<Article>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  marques:Marque[] = []


  constructor(
    private articleService:ArticleService,
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private dialog:MatDialog,
    private toast:ToastrService,
    private router:Router,
    private marqueService:MarqueService
  ) {

    // this.router.events.subscribe(event=>{
    //   event instanceof NavigationEnd? this.currentPage = event.url: null;

    //   event instanceof NavigationEnd? console.log(event): null;
    // })

    this.currentPage = this.router.url.split('/')[3]==null?'list':this.router.url.split('/')[3];
    console.log(this.router.url.split('/')[3])
      this.id = Number(this.router.url.split('/')[4]);
      this.articleList = this.articles.filter(art=>art.id===this.id)
      console.log(this.articleList)
  }

  ngOnInit(): void {
    this.findAll();
    this.findAllReception();
    this.findAllAffectation();
    this.findAllCategorie();
    this.findAllMarque();
    this.searchParam = new SearchParam();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }


  // applyFilter() {
  //   let filterValue = this.searchParam.query.trim(); // Remove whitespace
  //   filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
  //   this.dataSource.filter = filterValue;
  //   if (this.dataSource.paginator) {
  //     this.dataSource.paginator.firstPage();
  //   }
  // }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

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
        if (this.id) {
            this.findAffectationArticleByArticleId(this.id)
            this.findReceptionArticleByArticleId(this.id)
            this.article = this.articles.filter(art=>art.id===this.id)[0];
        }
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



  findAllMarque(){
    this.loading = true
    this.marqueService.findAll().subscribe(
      ret=>{
        if (ret["status"]===201 || ret["status"]===200) {
          this.marques = ret["body"];
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
        console.log("***************************")
        console.log(ret["body"])
      }else{
        console.log(environment.error_message)
      this.loading=false
      }
    },(err)=>{
      console.log(environment.error_message)
      this.loading=false
    })
  }



  findAllArticleByCategorieId(){
    if (this.searchParam.id) {
      console.log(this.searchParam)
      this.loading = true
      this.articleService.findAllArticleByCategorieId(this.searchParam).subscribe(ret=>{
        if (ret["status"]===201 || ret["status"]===200) {
          this.articles = ret["body"];
          console.log(this.articles)
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
  }

  findAllArticleByMarqueId(){
    if (this.searchParam.marqueId) {
      console.log(this.searchParam)
      this.loading = true
      this.articleService.findAllArticleByMarqueId(this.searchParam).subscribe(ret=>{
        if (ret["status"]===201 || ret["status"]===200) {
          this.articles = ret["body"];
          console.log(this.articles)
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
  }

  onSelection(data:Article,text:string){
    this.article = data;
    console.log(this.article)
    if (text==='detail') {
        this.findAffectationArticleByArticleId(this.article.id)
        this.findReceptionArticleByArticleId(this.article.id)
    }
    this.currentPage = text;

}
onListSelection(){this.currentPage='list'}
onDelete(data:Article){
  this.article = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "Voulez-Vous vraiment supprimer  cet Article";
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

findReceptionArticleByArticleId(id:number){
  this.receptionArticle = new ReceptionArticle()
  console.log(id)
   this.loading = true
    this.articleService.findReceptionArticleByArticleId(id).subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.receptionArticles = ret["body"];
        this.quantiteReceptionArticle = this.quantiteReceptionArticleCount();
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


findAffectationArticleByArticleId(id:number){
  this.receptionArticle = new ReceptionArticle()
  console.log(id)
   this.loading = true
    this.articleService.findAffectationArticleByArticleId(id).subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectationsArticles = ret["body"];
        this.quantiteAffectationArticle = this.quantiteAffectationArticleCount();
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

quantiteReceptionArticleCount(){
  return this.receptionArticles.reduce((acc,current)=>{return acc = acc + current.quantite},0)
}
quantiteAffectationArticleCount(){
  return this.affectationsArticles.reduce((acc,current)=>{return acc = acc + current.quantite},0)
}

onClear(){this.article = new Article();this.currentPage = 'list'}

}
