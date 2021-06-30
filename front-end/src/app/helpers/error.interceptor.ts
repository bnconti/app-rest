import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from '@services/authentication.service';

/**
 * Intercepta respuestas desde la API para verificar si hubo errores.
 * Actualmente solo intercepta el error 401, pero puede extenderse para otros casos.
 * 
 * Extiende de la clase HttpInterceptor, incluida en HttpClientModule. 
 * Se inserta al pipeline de requests en el archivo app.module.ts.
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401) {
                // cerrar sesión automáticamente si se recibe un 401
                this.authenticationService.logout();
                location.reload();
            }

            const error = err.error.message || err.statusText;
            return throwError(error);
        }))
    }
}
