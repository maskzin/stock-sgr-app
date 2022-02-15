import { SearchParam } from 'src/app/shared/models/search-param.model';
import { AffectationArticle } from './../models/affectation-article.model';
import { ReceptionArticle } from './../models/reception-article.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Article } from '../models/article.model';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Article[]>(this.apiUrl+'/articles',{observe:'response'});
    }

    save(artcile:Article){
      console.log("********Article********")
      console.log(artcile)
        return this.http.post<Article>(this.apiUrl+'/articles',artcile,{observe:'response'});
    }

    update(article:Article){
      return this.http.put<Article>(this.apiUrl+'/articles/'+article.id,article,{observe:'response'});
  }
  delete(article:Article){
    return this.http.delete<void>(this.apiUrl+'/articles/'+article.id,{observe:'response'});
  }

  findReceptionArticleByArticleId(id:number){
    return this.http.get<ReceptionArticle[]>(this.apiUrl+'/reception-articles/articleId/'+id,{observe:'response'});
  }

  findAffectationArticleByArticleId(id:number){
    return this.http.get<AffectationArticle[]>(this.apiUrl+'/affectation-articles/articleId/'+id,{observe:'response'});
  }

  findAllArticleByCategorieId(searchParam:SearchParam){
    console.log(searchParam)
  return this.http.get<Article[]>(this.apiUrl+'/articles/categorie/'+searchParam.id,{observe:'response'});
}

findAllArticleByMarqueId(searchParam:SearchParam){
  console.log(searchParam)
return this.http.get<Article[]>(this.apiUrl+'/articles/marque/'+searchParam.marqueId,{observe:'response'});
}



}
