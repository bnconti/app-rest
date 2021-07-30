import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { map } from "rxjs/operators";

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

  emailExists(email: string): Observable<HttpResponse<any>> {
    const url = `${environment.API_URL}/signup?email=${email}`;

    return this.http.get<any>(url)
      .pipe(map(res => {
        return res;
      }));
  }
}
