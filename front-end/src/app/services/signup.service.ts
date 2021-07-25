import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import {first, map} from "rxjs/operators";
import {User} from "@app/models/User";

@Injectable({
  providedIn: 'root'
})
export class SignupService {

  constructor(private http: HttpClient) {}

  signup(email: string, password: string): Observable<HttpResponse<any>> {
    const url = `${environment.API_URL}/signup`;
    const nuevoUsuario = { email, password };

    return this.http.post<any>(url, nuevoUsuario, { observe: 'response' })
      .pipe(map(res => {
        return res;
      }));
  }
}
