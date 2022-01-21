import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { Division } from 'src/app/shared/models/division.model';
import { Reception } from 'src/app/shared/models/reception.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { DivisionService } from 'src/app/shared/services/division.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-division',
  templateUrl: './division.component.html',
  styleUrls: ['./division.component.scss']
})
export class DivisionComponent implements OnInit {

  divisions :Division[]
  division:Division

  receptions:Reception[]
  reception:Reception

  affectations:Affectation[]
  affectation:Affectation

  // Divisions:Division[]
  // Division:Division
  currentPage:string="list"
  loading:boolean=false
  DivisionFilter: any = { libelleDivision: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['id', 'libelle', 'division', 'stock','actions'];
  dataSource = new MatTableDataSource<Division>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private divisionService:DivisionService,
    private dialog:MatDialog,
    private toast:ToastrService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.divisions)
    this.findAll();
    this.findAllReception();
    this.findAllAffectation();
    // this.findAllDivision();
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

  private initDataTable(cercles: Division[]) {
    this.dataSource = new MatTableDataSource(cercles);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAll(){
    this.loading = true
    this.divisionService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.divisions = ret["body"];
        this.initDataTable(this.divisions)
        this.loading = false
        this.toast.success(ret["body"].length+" Divisions ont été trouvés avec succès");
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

  // findAllDivision(){
  //   this.loading = true
  //   this.DivisionService.findAll().subscribe(ret=>{
  //     if (ret["status"]===201 || ret["status"]===200) {
  //       this.Divisions = ret["body"];
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

  onSelection(data:Division,text:string){
    this.division = data;
    console.log(this.division)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Division){
  this.division = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "cette Division";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,dialogConfig
  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      this.divisionService.delete(this.division).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success(this.division.nom+" a été supprimé avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.division = new Division();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.division = new Division();
  this.currentPage="add"
}
onSubmitDivision(){
  this.loading = true
  this.divisionService.save(this.division).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Division sauvegardé avec succès");
        this.loading = false
        this.findAll();
        this.onListSelection()
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.division = new Division();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditDivision(){
  console.log(this.division)
  this.loading = true
  this.divisionService.update(this.division).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Division enregistré avec succès");
      this.loading = false;
      this.findAll();
      this.onListSelection()
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.division = new Division();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.division = new Division();this.currentPage = 'list'}

}
