import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { Marque } from 'src/app/shared/models/marque.model';
import { Reception } from 'src/app/shared/models/reception.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { MarqueService } from 'src/app/shared/services/marque.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-marque',
  templateUrl: './marque.component.html',
  styleUrls: ['./marque.component.scss']
})
export class MarqueComponent implements OnInit {

  marques :Marque[] = []
  marque:Marque

  // Marque`s:Marque[]
  // Marque:Marque
  currentPage:string="list"
  loading:boolean=false
  MarqueFilter: any = { libelleMarque: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['libelle', 'marque', 'stock','actions'];
  dataSource = new MatTableDataSource<Marque>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private marqueService:MarqueService,
    private dialog:MatDialog,
    private toast:ToastrService
  ) { }

  ngOnInit(): void {
    this.findAll();
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

  private initDataTable(marques: Marque[]) {
    this.dataSource = new MatTableDataSource(marques);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAll(){
    this.loading = true
    this.marqueService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.marques = ret["body"];
        this.initDataTable(this.marques)
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
  onSelection(data:Marque,text:string){
    this.marque = data;
    console.log(this.marque)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Marque){
  this.marque = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "Voulez-Vous vraiment supprimer cette Catégorie";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,
    dialogConfig
  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.marqueService.delete(this.marque).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success(this.marque.libelle+" a été supprimé avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.marque = new Marque();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.marque = new Marque();
  this.currentPage="add"
}
onSubmitMarque(){
  this.loading = true
  this.marqueService.save(this.marque).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Categorie sauvegardé avec succès");
        this.loading = false
        this.onListSelection()
        this.findAll();
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.marque = new Marque();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditMarque(){
  this.loading = true
  this.marqueService.update(this.marque).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Categorie enregistré avec succès");
      this.loading = false;
      this.onListSelection()
      this.findAll();
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.marque = new Marque();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.marque = new Marque();this.currentPage = 'list'}

}
