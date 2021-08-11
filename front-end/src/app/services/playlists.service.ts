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

  getById(playlistId: string): Observable<Playlist> {
    const url = `${PlaylistsService.url}/${playlistId}`;
    return this.http.get<Playlist>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  existsByName(name: string): Observable<Boolean> {
    const url = `${PlaylistsService.url}/exists?name=${name}`;
    return this.http.get<Boolean>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  add(newPlaylist: Playlist): Observable<Playlist> {
    return this.http.post<Playlist>(PlaylistsService.url, newPlaylist)
      .pipe(map(res => {
        return res;
      }));
  }

  rename(playlistId: string, newName: string): Observable<Boolean> {
    const url = `${PlaylistsService.url}/${playlistId}`;
    return this.http.put<Boolean>(url, {"name": newName})
      .pipe(map(res => {
        return res;
      }));
  }

  delete(playlistId: string): Observable<Boolean> {
    const url = `${PlaylistsService.url}/${playlistId}`;
    return this.http.delete<Boolean>(url)
      .pipe(map(res => {
        return res;
      }));
  }

}
