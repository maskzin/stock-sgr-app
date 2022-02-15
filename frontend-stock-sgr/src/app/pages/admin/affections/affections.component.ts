import { AffectationArticleService } from './../../../shared/services/affectation-article.service';
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
import { FileUpload } from 'src/app/shared/models/file-upload.model';
import { MatTabGroup } from '@angular/material/tabs';


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
  affectationArticlesLists: AffectationArticle[] = []

  affectationArticleValids:AffectationArticle[] = []
  affectationArticleValid : AffectationArticle

  fournisseurs:Fournisseur[]=[]
  fournisseur:Fournisseur

  categories:Categorie[]=[]
  categorie:Categorie

  employees:Employee[]=[]
  employee:Employee
  affectationsLists:Affectation[] = []
  indexOfTabs:number= 0

  @ViewChild('fileUplaod') fileUplaod: any;
  @ViewChild(MatTabGroup) tabGroup: MatTabGroup;
  fileData:string
  file:FileUpload
  files:FileUpload[] = []

  currentPage:string="list"
  loading:boolean=false
  affectationFilter: any = { libelleArticle: '' };
  affectationValidsFilter:any = { reference:''}
  searchParam: SearchParam

  displayedColumns: string[] = ['categorie', 'stock','telephone','externe','actions'];
  dataSource = new MatTableDataSource<Affectation>();

  fileTypes:string[] = ["image/png","image/jpg","image/jpeg"]

  pdfSrc: FileUpload

  reaffectation:string='1'


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
    private afffectationArticleService:AffectationArticleService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.affectations)
    this.findAll();
    this.findAllFournisseur();
    this.findAllRecption();
    this.findAllCategorie();
    this.findAllArticles();
    this.findAllEmployees();
    this.findAllAffectationArticleByValidAt();
    this.searchParam = new SearchParam();
  }

  onSelectedTabs(event){
      console.log(event)
      this.indexOfTabs = event.index;
      if (event.index===0) {
          this.findAll();
      }
      if (event.index===1) {
        this.findAllAffectationsHistorique();
    }
  }



  onRemoveFile(id:number){
    console.log(id)
    this.files = this.files.filter((file,i)=>i!=id)
    this.pdfSrc = new FileUpload();
    console.log(this.files)
  }
  onSelectedFile() {
    this.fileUplaod.nativeElement.click();
  }

  onViewPDF(file:FileUpload){this.pdfSrc = file;console.log(this.pdfSrc)}

  processWebImage(event:any) {
    this.file = new FileUpload()
    if (event.target.files.length <= 0)
      return;
    let reader = new FileReader();

    reader.onload = (readerEvent) => {
      let imageData = (readerEvent.target as any).result;
      let imageSize = Number(event.target.files[0].size);
      imageSize = imageSize / 1024;
      if (imageSize > 1 * 1024 * 3) {
        // console.log('la taille de l\'image depasse 3 Mo');
        this.toast.error("La taille de l'image depasse 3 Mo","FERMER");
      } else {
        this.file.name = event.target.files[0].name
        this.file.taille = event.target.files[0].size
        this.file.type = event.target.files[0].type
        this.file.data = imageData;
      }

    };
    this.files.push(this.file);
    reader.readAsDataURL(event.target.files[0]);
  }


  applyFilter() {
    let filterValue = this.searchParam.query.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  private initDataTable(affec: Affectation[]) {
    this.dataSource = new MatTableDataSource(affec);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAllArticles(){
    this.loading = true
    this.articleService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.articles = ret["body"];
        this.loading = false
        console.log("*****Artciles*****")
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
  onMettreDansStock(affect:AffectationArticle){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = "Voulez-vous vraiment mettre ce article dans le stock ?";
    let dialogRef = this.dialog.open(DeleteArticleComponent,dialogConfig);
    dialogRef.afterClosed().subscribe(ret=>{
      if (ret==="ok") {
        this.loading = true
        this.afffectationArticleService.update(affect).subscribe(result=>{
          if (result["status"]===201 || result["status"]===200 ) {
              this.loading = false
              this.affectation.affectationArticles.filter(affectation=>affectation.id!=affect.id);
              this.findAll();
              this.onListSelection()
              this.toast.success("Article a été mis dans le Stock avec succès");
          }else{
            this.loading=false
            this.toast.error(environment.error_message);
          }
          this.affectation = new Affectation();
        },(err)=>{
          this.toast.error(environment.error_message);
        })
      }
  })}

  findAll(){
    this.loading = true
    this.affectationService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectations = ret["body"];
        this.initDataTable(this.affectations)
        this.loading = false
        this.findAllArticles()
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


  findAllAffectationsHistorique(){
    this.loading = true
    this.affectationService.findAllAffectationsHistorique().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectations = ret["body"];
        this.initDataTable(this.affectations)
        this.loading = false
        this.findAllArticles()
        this.toast.success(ret["body"].length+" Historiques d'affectation ont été trouvées avec succès");
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
  findAllAffectationArticleByValidAt(){
    this.loading = true
    this.affectationService.findAllAffectationArticleByValidAt().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectationArticleValids = ret["body"];
        console.log("*********Validity*********")
        console.log(this.affectationArticleValids)
        console.log("*********End*********")
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
summUpValidAtNull(affect:AffectationArticle[]){
    return affect.reduce((acc,affect)=>(acc + affect.quantite), 0)
}


  onSelection(data:Affectation,text:string){
    this.affectation = data;
    this.files = data.fileUploads;
    this.affectation.fileUploads = data.fileUploads
    this.affectation.affectationArticles = data.affectationArticles;
    this.affectationArticles = data.affectationArticles;
    this.affectationArticle = new AffectationArticle()
    this.article = new Article()
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

verifieArticleExist(){
  this.affectationArticles.map((receptArt,i)=>{
    if (this.affectationArticle.reference===receptArt.reference) {

       this.affectationArticle =  receptArt;
      //  this.articles.map(art=>{
      //    if(this.affectationArticle.article.id===art.id){

      //      this.affectationArticle.article.stock = art.stock;
      //    }
      //  })
       this.onDeleteReceptionArticle(i);
    }
  });
}



onListSelection(){
  this.pdfSrc = new FileUpload();
  this.currentPage='list';
}

onDelete(data:Affectation){
  this.affectation = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "cette Affecattion";
  let dialogRef = this.dialog.open(DeleteArticleComponent,dialogConfig);
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
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
  this.affectationArticles = []
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
  console.log("***********Affectation Article*************")
  console.log(this.affectationArticle)
  let result = this.affectationArticles.map(receptArt=>this.affectationArticle.article.id===receptArt.article.id)
  if (this.affectationArticle.article.type==='DURABLE') {
    this.affectationArticle.quantite = 1;
  }
  if (this.affectationArticle.quantite>this.affectationArticle.article.stock) {
      this.toast.error("Le nombre d'affectation ne doit pas être superieur à la quantité en stock")
  }else {

    this.affectationArticles.push(this.affectationArticle)
    this.affectationArticle = new AffectationArticle()
  }
}


onSubmitAffectation(){
  console.log("Add")
  console.log(this.affectation)
  this.affectation.affectationArticles = this.affectationArticles
  this.affectation.fileUploads = this.files;
  if (this.affectation.affectationArticles.length===0) {
    this.toast.error("Veuillez entrer un/des articles")
  } else {
    this.affectation.quantite = this.affectationArticles.reduce((acc,affect)=>(acc + affect.quantite), 0)
    this.loading = true
    this.affectationService.save(this.affectation).subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
          this.toast.success("Affectation sauvegardé avec succès");
          this.loading = false
          this.findAll();
          this.onListSelection()
          this.files=[];
          this.file = new FileUpload();
          this.pdfSrc = new FileUpload();
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
}

onEditAffectation(){
  console.log("Edit")
  console.log(this.affectation)
  this.affectation.affectationArticles = this.affectationArticles
  if (this.affectation.affectationArticles.length===0) {
    this.toast.error("Veuillez entrer un/des articles")
  } else {
    this.affectation.quantite = this.affectationArticles.reduce((acc,affect)=>(acc + affect.quantite), 0)
    console.log(this.affectation.quantite)
    this.loading = true
    this.affectationService.update(this.affectation).subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Affectation enregistré avec succès");
        this.loading = false;
        this.findAll();
        this.onListSelection()
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
}

onClear(){this.affectation = new Affectation();this.currentPage = 'list'}

}
