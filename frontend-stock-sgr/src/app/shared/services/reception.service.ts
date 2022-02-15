import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Reception } from '../models/reception.model';

@Injectable({
  providedIn: 'root'
})
export class ReceptionService {
  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Reception[]>(this.apiUrl+'/receptions',{observe:'response'});
    }
      save(reception:Reception){
        console.log("Service")
        console.log(reception)
          return this.http.post<Reception>(this.apiUrl+'/receptions',reception,{observe:'response'});
      }

      update(reception:Reception){
        return this.http.put<Reception>(this.apiUrl+'/receptions/'+reception.id,reception,{observe:'response'});
    }
    delete(reception:Reception){
      return this.http.delete<void>(this.apiUrl+'/receptions/'+reception.id,{observe:'response'});
    }
}
