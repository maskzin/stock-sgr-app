import { EmployeeService } from './../../../shared/services/employee.service';
import { Division } from './../../../shared/models/division.model';
import { DeleteArticleComponent } from './../../../components/delete-article/delete-article.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Affectation } from 'src/app/shared/models/affectation.model';
import { Employee } from 'src/app/shared/models/employee.model';
import { Categorie } from 'src/app/shared/models/categorie.model';
import { Reception } from 'src/app/shared/models/reception.model';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { environment } from 'src/environments/environment';
import { SearchParam } from 'src/app/shared/models/search-param.model';
import { ToastrService } from 'ngx-toastr';
import { DivisionService } from 'src/app/shared/services/division.service';


@Component({
  selector: 'app-employees',
  templateUrl: './employees.component.html',
  styleUrls: ['./employees.component.scss']
})
export class EmployeesComponent implements OnInit {

  employees :Employee[]=[]
  employee:Employee

  divisions:Division[] = []
  division:Division

  receptions:Reception[]=[]
  reception:Reception

  affectations:Affectation[]=[]
  affectation:Affectation

  categories:Categorie[]=[]
  categorie:Categorie
  currentPage:string="list"
  loading:boolean=false
  categorieFilter: any = { nom: '' };
  searchParam: SearchParam

  displayedColumns: string[] = ['libelle', 'categorie', 'stock','division','actions'];
  dataSource = new MatTableDataSource<Employee>();


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  constructor(
    private EmployeeService:EmployeeService,
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private dialog:MatDialog,
    private toast:ToastrService,
    private divisionService:DivisionService
  ) { }

  ngOnInit(): void {
    this.initDataTable(this.employees)
    this.findAll();
    this.findAllDivision();
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

  private initDataTable(cercles: Employee[]) {
    this.dataSource = new MatTableDataSource(cercles);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  findAll(){
    this.loading = true
    this.EmployeeService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.employees = ret["body"];
        this.initDataTable(this.employees)
        this.loading = false
        this.toast.success(ret["body"].length+" Employees ont été trouvés avec succès");
      }else{
        this.toast.error(environment.error_message);
        this.loading=false
      }

    },err=>{
      this.toast.error(environment.error_message);
      this.loading=false
    })
  }

  findAllEmployeesByDivision(){
    if (this.searchParam.id) {
      console.log(this.searchParam)
      this.loading = true
      this.EmployeeService.findAllEmployeesByDivision(this.searchParam).subscribe(ret=>{
        if (ret["status"]===201 || ret["status"]===200) {
          this.employees = ret["body"];
          this.initDataTable(this.employees)
          this.loading = false
          this.toast.success(ret["body"].length+" Employees ont été trouvés avec succès");
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

  findAllDivision(){
    this.loading = true
    this.divisionService.findAll().subscribe(
      ret=>{
        if (ret["status"]===201 || ret["status"]===200) {
          this.divisions = ret["body"];
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

  onSelection(data:Employee,text:string){
    this.employee = data;
    console.log(this.employee)
    this.currentPage = text;
}
onListSelection(){this.currentPage='list'}
onDelete(data:Employee){
  this.employee = data;
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = "Voulez-Vous vraiment supprimer cet Employé";
  let dialogRef = this.dialog.open(
    DeleteArticleComponent,
    dialogConfig
  );
  dialogRef.afterClosed().subscribe(ret=>{
    if (ret==="ok") {
      this.loading = true
      this.EmployeeService.delete(this.employee).subscribe(result=>{
        if (result["status"]===201 || result["status"]===200 ) {
            this.loading = false
            this.findAll();
            this.toast.success(this.employee.nom+" a été supprimé avec succès");
        }else{
          this.loading=false
          this.toast.error(environment.error_message);
        }
        this.employee = new Employee();
      },(err)=>{
        this.toast.error(environment.error_message);
      })
    }
  })
}
onAdd(){
  this.employee = new Employee();
  this.currentPage="add"
}
onSubmitEmployee(){
  this.loading = true
  this.EmployeeService.save(this.employee).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
        this.toast.success("Employee sauvegardé avec succès");
        this.loading = false;
        this.findAll();
        this.onListSelection()
    }else{
      this.loading=false
      this.toast.error(environment.error_message);
    }
    this.employee = new Employee();
  },(err)=>{
    this.loading=false
    this.toast.error(environment.error_message);
  })
}

onEditEmployee(){
  this.loading = true
  this.EmployeeService.update(this.employee).subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.toast.success("Employee enregistré avec succès");
      this.loading = false;
      this.findAll();
      this.onListSelection()
  }else{
    this.loading=false
    this.toast.error(environment.error_message);
  }
  this.employee = new Employee();
},(err)=>{
  this.loading=false
  this.toast.error(environment.error_message);
})
}

onClear(){this.employee = new Employee();this.currentPage = 'list'}


}
