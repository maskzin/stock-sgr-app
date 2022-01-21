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
        return this.http.post<Article>(this.apiUrl+'/articles',artcile,{observe:'response'});
    }

    update(article:Article){
      return this.http.put<Article>(this.apiUrl+'/articles/'+article.id,article,{observe:'response'});
  }
  delete(article:Article){
    return this.http.delete<void>(this.apiUrl+'/articles/'+article.id,{observe:'response'});
  }


}
