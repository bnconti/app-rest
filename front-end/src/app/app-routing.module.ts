import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from "@app/components/home/home.component";
import { PlaylistsComponent } from "@app/components/home/playlists/playlists.component";
import { LoginComponent } from "@app/components/login/login.component";
import { SignupComponent } from "@app/components/signup/signup.component";
import { AuthGuard } from "@app/helpers/auth.guard";
import {SongsComponent} from "@app/components/songs/songs.component";
import {AddEditSongComponent} from "@app/components/songs/add-edit-song/add-edit-song.component";

/**
 * El enrutamiento de la aplicación se configura a partir del arreglo 'routes'. Cada ruta es mapeada
 * hacia un componente, de manera que al ingresar a cierta URL el Router mostrará cierto componente.
 *
 * Las rutas son protegidas pasando a la propiedad canActivate el AuthGuard.
 *
 * A su vez, el arreglo de routes se pasa al método RouterModule.forRoot(), el cual crea un módulo
 * que va a contener todas las rutas configuradas a mano o propias de Angular (como el router-outlet).
 *
 * https://angular.io/guide/router.
 */

const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'home/playlists', component: PlaylistsComponent, canActivate: [AuthGuard] },
  { path: 'home/songs', component: SongsComponent, canActivate: [AuthGuard] },
  { path: 'home/songs/new', component: AddEditSongComponent, canActivate: [AuthGuard] },
  { path: 'home/songs/:id', component: AddEditSongComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },

  // si se recibe cualquier otra ruta, redirigimos a Home
  { path: '**', redirectTo: 'home' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
