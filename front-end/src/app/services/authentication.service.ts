import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { User } from '@app/models/User';

/**
 * El servicio de autenticación es utilizado para iniciar o cerrar sesión.
 *
 * El servicio implementa objetos observadores y observables de RxJS, los cuales almacenan el usuario y son los
 * encargados de noticiar a los objetos subscriptos (por ej. de que el usuario cerró sesión).
 *
 * Para subscribirse, el objeto tiene que invocar el método subscribe() sobre la propiedad currentUser (de tipo
 * Observable). Las notificaciones son enviadas luego de invocar al método currentUserSubject.next() en los
 * métodos login() y logout().
 *
 * La propiedad currentUserValue le permite a otros objetos obtener fácilmente el usuario actualmente logueado,
 * sin que sea necesario que se subscriban al observador.
 *
 * El constructor() inicializa la propiedad currentUserSubjet con lo que está guardado localmente, esto permite al
 * usuario refrescar una página o reabrir el navegador sin necesidad de iniciar de nuevo sesión.
 * La propiedad currentUser se define en función del observador de currentUserSubject, lo que le permite a otros
 * componentes subscribirse para obtener el usuario actual, pero NO generar eventos como inicio o cierre de sesión,
 * acciones que se llevan a cabo en la propiedad privada currentUserSubject.
 *
 * El método login() envía las credenciales del usuario a la API mediante un request de tipo post.
 * Si el login tiene éxito, se almacena localmente el usuario con su JWT. Luego, se publica el objeto
 * User a los subscriptores con la llamada al método next().
 *
 * El método logout() elimina el objeto User actual del almacenamiento local y notifica mediante un objeto nulo
 * a los subscriptores de que el usuario cerró sesión.
 */
@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    // private currentUserSubject: BehaviorSubject<User>;
    // public currentUser: Observable<User>;

    constructor(private http: HttpClient) {
        // this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')!));
        // this.currentUser = this.currentUserSubject.asObservable();
    }

    /*
    public get currentUserValue(): User | null {
        return this.currentUserSubject.value;
    }
    */

    login(email: string, password: string): Observable<HttpResponse<any>> {
        const url = `${environment.API_URL}/auth`;
        const credentials = { email, password };
        
        return this.http.post<any>(url, credentials, { observe: 'response' })
            .pipe(map(res => {
                console.log(res.headers.keys())
                if (res.headers.get("Authorization")) {
                    // guardar el JWT y otros detalles en el localStorage para preservar la sesión del usuario
                    localStorage.setItem('JWT', JSON.stringify(res.headers.get("Authorization")));
                    
                    // actualizar los subscriptores con el usuario logueado
                    // this.currentUserSubject!.next(res);
                }
                return res;
            }));
    }

    logout() {
        // remover token del localStorage
        localStorage.removeItem('JWT');
        // notificar a los subscriptores de que el usuario cerró sesión
        // this.currentUserSubject!.next(new User(null, null, null));
    }
}
