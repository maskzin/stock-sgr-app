import { Reception } from 'src/app/shared/models/reception.model';
import { Fournisseur } from './../../../shared/models/fournisseur.model';
import { Categorie } from './../../../shared/models/categorie.model';
import { Employee } from 'src/app/shared/models/employee.model';
import { MatTabGroup } from '@angular/material/tabs';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { FournisseurService } from './../../../shared/services/fournisseur.service';
import { EmployeeService } from './../../../shared/services/employee.service';
import { ToastrService } from 'ngx-toastr';
import { ArticleService } from './../../../shared/services/article.service';
import { ReceptionService } from './../../../shared/services/reception.service';
import { AffectationArticleService } from './../../../shared/services/affectation-article.service';
import { SearchParam } from './../../../shared/models/search-param.model';
import { MatTableDataSource } from '@angular/material/table';
import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Article } from './../../../shared/models/article.model';
import { AffectationArticle } from './../../../shared/models/affectation-article.model';
import { FileUpload } from 'src/app/shared/models/file-upload.model';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { environment } from './../../../../environments/environment';
import { Component, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-epaves',
  templateUrl: './epaves.component.html',
  styleUrls: ['./epaves.component.scss']
})
export class EpavesComponent implements OnInit {


  affectations :Affectation[]=[]
  affectation:Affectation

  articles:Article[]=[]
  article:Article

 receptions:Reception[]=[]
 reception:Reception


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

  displayedColumns: string[] = ['telephone','categorie','marque','stock'];
  dataSource = new MatTableDataSource<AffectationArticle>();

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
    this.findAll();
    this.searchParam = new SearchParam();
  }


  onViewPDF(file:FileUpload){this.pdfSrc = file;console.log(this.pdfSrc)}

  applyFilter() {
    let filterValue = this.searchParam.query.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  private initDataTable(affec: AffectationArticle[]) {
    this.dataSource = new MatTableDataSource(affec);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  findAll(){
    this.loading = true
    this.afffectationArticleService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.affectationArticles = ret["body"];
        this.initDataTable(this.affectationArticles)
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
onClear(){this.affectation = new Affectation();this.currentPage = 'list'}

}
