import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { Fournisseur } from 'src/app/shared/models/fournisseur.model';
import { Categorie } from 'src/app/shared/models/categorie.model';
import { Reception } from 'src/app/shared/models/reception.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { FournisseurService } from 'src/app/shared/services/fournisseur.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-fournisseurs',
  templateUrl: './fournisseurs.component.html',
  styleUrls: ['./fournisseurs.component.scss']
})
export class FournisseursComponent implements OnInit {

  fournisseurs :Fournisseur[]
  fournisseur:Fournisseur

  receptions:Reception[]
  reception:Reception

  affectations:Affectation[]
  affectation:Affectation

  categories:Categorie[]
  categorie:Categorie
  currentPage:string="list"
  loading:boolean=false
  FournisseurFilter: any = { libelleFournisseur: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['id', 'libelle', 'categorie', 'stock','actions'];
  dataSource = new MatTableDataSource<Fournisseur>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private fournisseurService:FournisseurService,
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private dialog:MatDialog,
    private toast:ToastrService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.fournisseurs)
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

  private initDataTable(cercles: Fournisseur[]) {
    this.dataSource = new MatTableDataSource(cercles);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAll(){
    this.loading = true
    this.fournisseurService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.fournisseurs = ret["body"];
        this.initDataTable(this.fournisseurs)
        this.loading = false
        this.toast.success(ret["body"].length+" Fournisseurs ont été trouvés avec succès");
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

  onSelection(data:Fournisseur,text:string){
    this.fournisseur = data;
    console.log(this.fournisseur)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Fournisseur){
  this.fournisseur = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "ce Fournisseur";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,
   dialogConfig
  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      this.fournisseurService.delete(this.fournisseur).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success(this.fournisseur.nom+" a été supprimé avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.fournisseur = new Fournisseur();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.fournisseur = new Fournisseur();
  this.currentPage="add"
}
onSubmitFournisseur(){
  this.loading = true
  this.fournisseurService.save(this.fournisseur).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Fournisseur sauvegardé avec succès");
        this.loading = false
        this.findAll();
        this.onListSelection()
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.fournisseur = new Fournisseur();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditFournisseur(){
  this.loading = true
  this.fournisseurService.update(this.fournisseur).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Fournisseur enregistré avec succès");
      this.loading = false;
      this.findAll();
      this.onListSelection()
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.fournisseur = new Fournisseur();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.fournisseur = new Fournisseur();this.currentPage = 'list'}

}
