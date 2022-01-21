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
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss']
})
export class CategoriesComponent implements OnInit {


  categories :Categorie[]
  categorie:Categorie

  receptions:Reception[]
  reception:Reception

  affectations:Affectation[]
  affectation:Affectation

  // categories:Categorie[]
  // categorie:Categorie
  currentPage:string="list"
  loading:boolean=false
  CategorieFilter: any = { libelleCategorie: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['id', 'libelle', 'categorie', 'stock','actions'];
  dataSource = new MatTableDataSource<Categorie>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private dialog:MatDialog,
    private toast:ToastrService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.categories)
    this.findAll();
    this.findAllReception();
    this.findAllAffectation();
    // this.findAllCategorie();
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

  private initDataTable(cercles: Categorie[]) {
    this.dataSource = new MatTableDataSource(cercles);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAll(){
    this.loading = true
    this.categorieService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.categories = ret["body"];
        this.initDataTable(this.categories)
        this.loading = false
        this.toast.success(ret["body"].length+" Categories ont été trouvés avec succès");
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

  // findAllCategorie(){
  //   this.loading = true
  //   this.categorieService.findAll().subscribe(ret=>{
  //     if (ret["status"]===201 || ret["status"]===200) {
  //       this.categories = ret["body"];
  //       this.loading = false
  //     }else{
  //       console.log(environment.error_message)
  //     this.loading=false
  //     }
  //   },(err)=>{
  //     console.log(environment.error_message)
  //     this.loading=false
  //   })
  // }

  onSelection(data:Categorie,text:string){
    this.categorie = data;
    console.log(this.categorie)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Categorie){
  this.categorie = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "cette Catégorie";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,
    dialogConfig
  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      this.categorieService.delete(this.categorie).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success(this.categorie.nom+" a été supprimé avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.categorie = new Categorie();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.categorie = new Categorie();
  this.currentPage="add"
}
onSubmitCategorie(){
  this.loading = true
  this.categorieService.save(this.categorie).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Categorie sauvegardé avec succès");
        this.loading = false
        this.findAll();
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.categorie = new Categorie();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditCategorie(){
  this.loading = true
  this.categorieService.update(this.categorie).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Categorie enregistré avec succès");
      this.loading = false;
      this.findAll();
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.categorie = new Categorie();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.categorie = new Categorie();this.currentPage = 'list'}


}
