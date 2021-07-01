import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { User } from '@app/models/User';

/**
 * El servicio de autenticación es utilizado para iniciar o cerrar sesión.
 * *
 * El método login() envía las credenciales del usuario a la API mediante un request de tipo post.
 * Si el login tiene éxito, se almacena localmente el usuario con su JWT.
 *
 * El método logout() elimina el objeto User actual del almacenamiento local.
 */
@Injectable({ providedIn: 'root' })
export class AuthenticationService {

    constructor(private http: HttpClient) {}

    login(email: string, password: string): Observable<HttpResponse<any>> {
        const url = `${environment.API_URL}/auth`;
        const credentials = { email, password };
        
        return this.http.post<any>(url, credentials, { observe: 'response' })
            .pipe(map(res => {
                if (res.headers.get("Authorization")) {
                    const user = new User(credentials.email, res.headers.get("Authorization")!);

                    // guardar el usuario en el localStorage para preservar su sesión
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }
                return res;
            }));
    }

    logout() {
        // remover token del localStorage
        localStorage.removeItem('currentUser');
    }
}
