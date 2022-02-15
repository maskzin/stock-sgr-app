import { AffectationArticle } from './../../../shared/models/affectation-article.model';
import { AffectationArticleService } from './../../../shared/services/affectation-article.service';
import { Division } from 'src/app/shared/models/division.model';
import { DivisionService } from 'src/app/shared/services/division.service';
import { Fournisseur } from './../../../shared/models/fournisseur.model';
import { Employee } from 'src/app/shared/models/employee.model';
import { EmployeeService } from 'src/app/shared/services/employee.service';
import { FournisseurService } from './../../../shared/services/fournisseur.service';
import { CategorieService } from 'src/app/shared/services/categorie.service';
import { AffectationService } from 'src/app/shared/services/affectation.service';
import { ReceptionService } from 'src/app/shared/services/reception.service';
import { ArticleService } from 'src/app/shared/services/article.service';
import { Categorie } from './../../../shared/models/categorie.model';
import { Affectation } from './../../../shared/models/affectation.model';
import { Reception } from './../../../shared/models/reception.model';
import { Article } from './../../../shared/models/article.model';
import { Component, Input, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Chart, registerables} from 'chart.js';
import { Observable } from 'rxjs';
// import { Chart, BarElement, BarController, CategoryScale, Decimation, Filler, Legend, Title, Tooltip} from 'chart.js';

Chart.register(...registerables);


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  articles :Article[]= []
  article:Article

  receptions:Reception[]= []

  employees:Employee[]=[]

  fournisseurs:Fournisseur[] = []

  divisions:Division[]= []


  affectations:Affectation[]= []

  affectationArticles:AffectationArticle[]= [];


  categories:Categorie[]= []

  loading:boolean=false

  currentPage:string="list"
  view:string ='stats'
  public myChart: Chart


  constructor(
    private articleService:ArticleService,
    private receptionService:ReceptionService,
    private affectationService:AffectationService,
    private categorieService:CategorieService,
    private fournisseurService:FournisseurService,
    private employeeService:EmployeeService,
    private divisionService:DivisionService,
    private afffectationArticleService:AffectationArticleService

  ) {
    // Chart.register(BarElement, BarController, CategoryScale, Decimation, Filler, Legend, Title, Tooltip);
  }

  ngOnInit(): void {
    this.findAll();
    this.findAllReception();
    this.findAllAffectation();
    this.findAllCategorie();
    this.findAllFournisseur();
    this.findAllEmployee();
    this.findAllDivision();
    this.findAllEpave();
  }
findAll(){
  this.loading = true
  this.articleService.findAll().subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.articles = ret["body"];
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

findAllEmployee(){
  this.loading = true
  this.employeeService.findAll().subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.employees = ret["body"];
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


findAllEpave(){
  this.loading = true
  this.afffectationArticleService.findAll().subscribe(ret=>{
    if (ret["status"]===201 || ret["status"]===200) {
      this.affectationArticles = ret["body"];
      this.loading = false
    }else{
      console.log(environment.error_message);
      this.loading=false
    }

  },err=>{
    console.log(environment.error_message);
    this.loading=false
  })
}

findAllFournisseur(){
  this.loading = true
  this.fournisseurService.findAll().subscribe(ret=>{
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
findAllReception(){
  this.loading = true
  this.receptionService.findAll().subscribe(
    ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.receptions = ret["body"];
        console.log("Reception")
        console.log(ret["body"])
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
findAllDivision(){
  this.loading = true
    this.divisionService.findAll().subscribe(ret=>{
      if (ret["status"]===201 || ret["status"]===200) {
        this.divisions = ret["body"];
        this.loading = false
      }else{
        this.loading=false
      }

    },err=>{
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

stockArticleCount(articles:Article[]){
  let sumStock = 0;
    articles.forEach(article => {
        sumStock += Number(article.stock);
    });
    return sumStock;
}



}
