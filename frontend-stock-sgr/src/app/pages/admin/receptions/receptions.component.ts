import { FileUpload } from 'src/app/shared/models/file-upload.model';
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


  @ViewChild('fileUplaod') fileUplaod: any;
  fileData:string
  file:FileUpload
  files:FileUpload[] = []


  currentPage:string='list'
  loading:boolean=false
  ReceptionFilter: any = { libelleReception: '' };
  searchParam: SearchParam
  quantiteArticleReceptionArticle:number

  displayedColumns: string[] = ['libelle', 'stock','telephone','actions'];
  dataSource = new MatTableDataSource<Reception>();

  articleFilter: any = { libelleArticle: '' };

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  fileTypes:string[] = ["image/png","image/jpg","image/jpeg"]

  pdfSrc: FileUpload


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
    this.receptionArticles = data.receptionArticles;
    console.log(this.reception)
    this.currentPage = text;
}
onListSelection(){
  this.pdfSrc = new FileUpload();
    this.currentPage='list'

  }
onDelete(data:Reception){
  this.reception = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "Voulez-Vous vraiment supprimer cette Reception";
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
  this.receptionArticles = []
  this.reception = new Reception();
  this.currentPage="add"
  this.reception = new Reception();
  this.receptionArticle = new ReceptionArticle()
}
onDeleteReceptionArticle(id:number){
    this.receptionArticles= this.receptionArticles.filter((recept,i)=>i!=id)
}

verifieArticleExist(){
  this.receptionArticles.map((receptArt,i)=>{
    if (this.receptionArticle.article.id===receptArt.article.id) {
       this.receptionArticle =  receptArt;
       this.onDeleteReceptionArticle(i);
    }
  });
}

onAddReceptionArticle(){
  console.log(this.receptionArticle)
  console.log(this.receptionArticles)
  let result = this.receptionArticles.map(receptArt=>this.receptionArticle.article.id===receptArt.article.id)
  console.log(result)
  this.receptionArticles.push(this.receptionArticle)
  this.receptionArticle = new ReceptionArticle()
}

quantiteArticleByReceptionArticle(){
  this.quantiteArticleReceptionArticle =  this.reception.receptionArticles.reduce((acc,item)=>{return acc+item.quantite},0)
}

onSubmitReception(){
  // this.reception.articles = [this.article];
  this.reception.receptionArticles = this.receptionArticles;
  this.reception.fileUploads = this.files;
  // this.reception.articles.map(art=>this.saveArticle(art));
  if (this.reception.receptionArticles.length===0) {
    this.toast.error("Veuillez entrer un/des articles")
  } else {
    this.loading = true
    this.reception.quantite = this.receptionArticles.reduce((acc,affect)=>(acc + affect.quantite), 0)
    console.log(this.reception)
    this.receptionService.save(this.reception).subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
          this.toast.success("Reception sauvegardé avec succès");
          this.loading = false
          this.receptionArticles=[]
          console.log("Response")
          console.log(ret["body"])
          this.findAll();
          this.onListSelection()
          this.files=[];
          this.file = new FileUpload();
          this.pdfSrc = new FileUpload();
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
  this.reception.receptionArticles = this.receptionArticles;
  if (this.reception.receptionArticles.length===0) {
    this.toast.error("Veuillez entrer un/des articles")
  } else {
    this.loading = true
    this.reception.quantite = this.receptionArticles.reduce((acc,affect)=>(acc + affect.quantite), 0)
    // console.log(this.reception)
    this.receptionService.update(this.reception).subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Reception enregistré avec succès");
        this.loading = false;
        this.findAll();
        this.onListSelection()
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
