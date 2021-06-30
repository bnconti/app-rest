import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { AuthenticationService } from '@services/authentication.service';

/**
 * El auth guard previene accesos no autorizado sobre las rutas que se quiere proteger. Para esto,
 * implementa la interfaz CanActivate con su método canActivate(), que retorna verdadero cuando una
 * ruta está permitida para cierto usuario. Caso contrario, retorna falso y el acceso se bloquea.
 * 
 * Para verificar si un usuario inició sesión, el auth guard utiliza el servicio de autenticación.
 * 
 * El auth guard se aplica sobre las rutas deseadas en el archivo de configuración app-routing.module.ts
 */

 @Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

        const JWT = localStorage.getItem("JWT");

        if (JWT) {
            // si hay un JWT seteado es porque logueó, dejarlo pasar
            return true;
        } else {
            // sino lo mando pa'l login
            this.router.navigate(['/login']);
            return false;
        }

        /*
        const currentUser = this.authenticationService.currentUserValue;
        if (currentUser) {
            // está logueado, retornar verdadero
            return true;
        }

        // no está logueado, redirigir al usuario a la página de login
        this.router.navigate(['/login']);
        return false;
        */
    }
}
