import { SearchParam } from './../models/search-param.model';
import { Employee } from './../models/employee.model';
import { environment } from './../../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  apiUrl:string = environment.apiUrl;
  constructor(
    private http:HttpClient
  ) { }


    findAll(){
      return this.http.get<Employee[]>(this.apiUrl+'/employees',{observe:'response'});
    }

    save(employee:Employee){
        return this.http.post<Employee>(this.apiUrl+'/employees',employee,{observe:'response'});
    }

    update(employee:Employee){
        return this.http.put<Employee>(this.apiUrl+'/employees/'+employee.id,employee,{observe:'response'});
    }
    delete(employee:Employee){
      return this.http.delete<void>(this.apiUrl+'/employees/'+employee.id,{observe:'response'});
    }
    findAllEmployeesByDivision(searchParam:SearchParam){
        console.log(searchParam)
      return this.http.get<Employee[]>(this.apiUrl+'/employees/division/'+searchParam.id,{observe:'response'});
    }
}
