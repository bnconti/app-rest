import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { environment } from '@environments/environment';
import { Playlist } from '@app/models/Playlist';

/**
 * Servicio de playlists.
 *
 * Obtiene las playlists del servicio REST.
 */
@Injectable({ providedIn: 'root' })
export class PlaylistsService {
  constructor(private http: HttpClient) {}

  getPlaylists(): Observable<Playlist[]> {
    const url = `${environment.API_URL}/playlists`

    return this.http.get<Playlist[]>(url)
      .pipe(map(res => {
	return res;
      }));
  }
}
