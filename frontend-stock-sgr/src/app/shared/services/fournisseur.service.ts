import { Fournisseur } from './../models/fournisseur.model';
import { environment } from './../../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FournisseurService {
  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Fournisseur[]>(this.apiUrl+'/fournisseurs',{observe:'response'});
    }

    save(forunisseur:Fournisseur){
        return this.http.post<Fournisseur>(this.apiUrl+'/fournisseurs',forunisseur,{observe:'response'});
    }

    update(article:Fournisseur){
      return this.http.put<Fournisseur>(this.apiUrl+'/fournisseurs/'+article.id,article,{observe:'response'});
  }
  delete(forunisseur:Fournisseur){
    return this.http.delete<void>(this.apiUrl+'/fournisseurs/'+forunisseur.id,{observe:'response'});
  }
}
