import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Affectation } from '../models/affectation.model';

@Injectable({
  providedIn: 'root'
})
export class AffectationService {
  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Affectation[]>(this.apiUrl+'/affectations',{observe:'response'});
    }

    save(affectation:Affectation){
      console.log("Service")
      console.log(affectation)
      return this.http.post<Affectation>(this.apiUrl+'/affectations',affectation,{observe:'response'});
  }

  update(affectation:Affectation){
    return this.http.put<Affectation>(this.apiUrl+'/affectations/'+affectation.id,affectation,{observe:'response'});
}
delete(affectation:Affectation){
  return this.http.delete<void>(this.apiUrl+'/affectations/'+affectation.id,{observe:'response'});
}
}
