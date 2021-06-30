import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '@environments/environment';
import { AuthenticationService } from '@services/authentication.service';

/**
 * Intercepta respuestas desde la API para añadir el JWT al header Authorization.
 *
 * Extiende de la clase HttpInterceptor, incluida en HttpClientModule.
 * Se inserta al pipeline de requests en el archivo app.module.ts.
 */
/*
@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthenticationService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // añade el header auth con el JWT si el usuario está logueado y es una dirección de la API
        const currentUser = this.authenticationService.currentUserValue;
        const isLoggedIn = currentUser && currentUser.token;
        const isApiUrl = request.url.startsWith(environment.API_URL);
        if (isLoggedIn && isApiUrl) {
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${currentUser!.token}`
                }
            });
        }

        return next.handle(request);
    }
}
*/
