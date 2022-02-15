import { AffectationArticle } from './../models/affectation-article.model';
import { environment } from './../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AffectationArticleService {


  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }

  findAll(){
    return this.http.get<AffectationArticle[]>(this.apiUrl+'/affectation-articles/epave',{observe:'response'});
  }

  save(affect:AffectationArticle){
      return this.http.post<AffectationArticle>(this.apiUrl+'/affectation-articles',affect,{observe:'response'});
  }

  update(affect:AffectationArticle){
    return this.http.put<AffectationArticle>(this.apiUrl+'/affectation-articles/'+affect.id,affect,{observe:'response'});
}
delete(affect:AffectationArticle){
  return this.http.delete<void>(this.apiUrl+'/affectation-articles/'+affect.id,{observe:'response'});
}
}
