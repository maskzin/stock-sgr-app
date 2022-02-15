import { environment } from './../../../environments/environment';
import { Division } from './../models/division.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class DivisionService {

  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }

  findAll(){
    return this.http.get<Division[]>(this.apiUrl+'/divisions',{observe:'response'});
  }

  save(division:Division){
      return this.http.post<Division>(this.apiUrl+'/divisions',division,{observe:'response'});
  }

  update(division:Division){
    return this.http.put<Division>(this.apiUrl+'/divisions/'+division.id,division,{observe:'response'});
}
delete(division:Division){
  return this.http.delete<void>(this.apiUrl+'/divisions/'+division.id,{observe:'response'});
}
}
