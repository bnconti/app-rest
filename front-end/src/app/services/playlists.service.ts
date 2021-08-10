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
@Injectable({
  providedIn: 'root'
})
export class PlaylistsService {

  private static readonly url = `${environment.API_URL}/playlists`;
  private static readonly userUrl = `${environment.API_URL}/userplaylists`;

  constructor(private http: HttpClient) {
  }

  getUserPlaylists(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(PlaylistsService.userUrl)
      .pipe(map(res => {
	return res;
      }));
  }

  getAllPlaylists(): Observable<Playlist[]> {
    return this.http.get<Playlist[]>(PlaylistsService.url)
      .pipe(map(res => {
	return res;
      }));
  }

  getById(playlistId: bigint): Observable<Playlist> {
    const url = `${PlaylistsService.url}/find/${playlistId}`;
    return this.http.get<Playlist>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  delete(playlistId: bigint): Observable<Boolean> {
    const url = `${PlaylistsService.url}/${playlistId}`;
    return this.http.delete<Boolean>(url)
      .pipe(map(res => {
        return res;
      }));
  }

}
