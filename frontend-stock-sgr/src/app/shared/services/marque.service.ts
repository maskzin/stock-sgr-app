import { Marque } from './../models/marque.model';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MarqueService {

  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Marque[]>(this.apiUrl+'/marques',{observe:'response'});
    }
    save(categorie:Marque){
      return this.http.post<Marque>(this.apiUrl+'/marques',categorie,{observe:'response'});
  }

  update(categorie:Marque){
    return this.http.put<Marque>(this.apiUrl+'/marques/'+categorie.id,categorie,{observe:'response'});
}
delete(categorie:Marque){
  return this.http.delete<void>(this.apiUrl+'/marques/'+categorie.id,{observe:'response'});
}
}
