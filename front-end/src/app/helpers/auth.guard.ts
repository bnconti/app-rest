import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

/**
 * El auth guard previene accesos no autorizado sobre las rutas que se quiere proteger. Para esto,
 * implementa la interfaz CanActivate con su método canActivate(), que retorna verdadero cuando una
 * ruta está permitida para cierto usuario si se cumple cierta condición. Caso contrario, retorna
 * falso y el acceso se bloquea.
 *
 * El auth guard se aplica sobre las rutas deseadas (por ej. /home) en el archivo de configuración
 * app-routing.module.ts
 */

 @Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
    ) { }

    canActivate() {
        if (sessionStorage.getItem("currentUser")) {
            // si hay un usuario guardado es porque pudo ingresar, dejarlo pasar
            return true;
        } else {
            // sino lo mando al login
            this.router.navigate(['/login']);
            return false;
        }
    }
}
