import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Categorie } from '../models/categorie.model';

@Injectable({
  providedIn: 'root'
})
export class CategorieService {

  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Categorie[]>(this.apiUrl+'/categories',{observe:'response'});
    }
    save(categorie:Categorie){
      return this.http.post<Categorie>(this.apiUrl+'/categories',categorie,{observe:'response'});
  }

  update(categorie:Categorie){
    return this.http.put<Categorie>(this.apiUrl+'/categories/'+categorie.id,categorie,{observe:'response'});
}
delete(categorie:Categorie){
  return this.http.delete<void>(this.apiUrl+'/categories/'+categorie.id,{observe:'response'});
}
}
