import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '@environments/environment';

/**
 * Intercepta respuestas desde la API para añadir el JWT al header Authorization
 * si el usuario está logueado.
 *
 * Extiende de la clase HttpInterceptor, incluida en HttpClientModule.
 * Se inserta al pipeline de requests en el archivo app.module.ts.
 */

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const isLoggedIn = localStorage.getItem('currentUser') != null;
        const isApiUrl = request.url.startsWith(environment.API_URL);
        if (isLoggedIn && isApiUrl) {
            const token = JSON.parse(localStorage.getItem('currentUser')!).token;
            request = request.clone({
                setHeaders: {
                    Authorization: `${token}`
                }
            });
        }

        return next.handle(request);
    }
}
