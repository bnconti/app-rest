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

  getIdByUserAndName(userEmail: string, name: string): Observable<string> {
    const url = `${PlaylistsService.url}/getid?user=${userEmail}&name=${name}`;
    return this.http.get<string>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  add(newPlaylist: Playlist): Observable<string> {
    return this.http.post<string>(PlaylistsService.url, newPlaylist)
      .pipe(map(res => {
        return res;
      }));
  }

  rename(playlistId: string, newName: string): Observable<void> {
    const url = `${PlaylistsService.url}/${playlistId}`;
    return this.http.put<void>(url, {name: newName})
      .pipe(map(res => {
        return res;
      }));
  }

  addSong(playlistId: string, songId: string): Observable<Boolean> {
    const url = `${PlaylistsService.url}/${playlistId}/songs`;
    return this.http.put<Boolean>(url, {songId: songId})
      .pipe(map(res => {
        return res;
      }));
  }

  removeSong(playlistId: string, songId: string): Observable<void> {
    const url = `${PlaylistsService.url}/${playlistId}/songs/${songId}`;
    return this.http.delete<void>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  delete(playlistId: string): Observable<boolean> {
    const url = `${PlaylistsService.url}/${playlistId}`;
    return this.http.delete<boolean>(url)
      .pipe(map(res => {
        return res;
      }));
  }

}
