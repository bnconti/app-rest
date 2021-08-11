import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Song} from "@app/models/Song";
import {environment} from "@environments/environment";
import {map} from "rxjs/operators";
import {Genre} from "@app/models/Genre";

@Injectable({
  providedIn: 'root'
})
export class SongsService {

  private static readonly url = `${environment.API_URL}/songs`;

  constructor(private http: HttpClient) {
  }

  getSongs(): Observable<Song[]> {
    return this.http.get<Song[]>(SongsService.url)
      .pipe(map(res => {
        return res
      }));
  }

  getAuthors(): Observable<String[]> {
    const url = `${SongsService.url}/authors`;
    return this.http.get<String[]>(url)
      .pipe(map(res => {
        return res
      }));
  }

  getGenres(): Genre[] {
    return [
      {id: 0, desc: 'ROCK'},
      {id: 1, desc: 'TECHNO'},
      {id: 2, desc: 'POP'},
      {id: 3, desc: 'JAZZ'},
      {id: 4, desc: 'FOLK'},
      {id: 5, desc: 'CLASSICAL'},
      {id: 6, desc: 'REGGAE'},
      {id: 7, desc: 'CUMBIA'}
    ];
  }

  getById(songId: string): Observable<Song> {
    const url = `${SongsService.url}/${songId}`;
    return this.http.get<Song>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  existsByAuthorAndName(author: string, name: string): Observable<Boolean> {
    const url = `${SongsService.url}/exists?author=${author}&name=${name}`;
    return this.http.get<any>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  add(newSong: Song): Observable<Song> {
    return this.http.post<Song>(SongsService.url, newSong)
      .pipe(map(res => {
        return res;
      }));
  }

  update(updatedSong: Song): Observable<Song> {
    return this.http.put<Song>(SongsService.url, updatedSong)
      .pipe(map(res => {
        return res;
      }));
  }

  delete(songId: string): Observable<Boolean> {
    const url = `${SongsService.url}/${songId}`;
    return this.http.delete<Boolean>(url)
      .pipe(map(res => {
        return res;
      }));
  }

  getByAuthorAndName(author: string, name: string): Observable<Song> {
    const url = `${SongsService.url}/find?author=${author}&name=${name}`;
    return this.http.get<Song>(url)
      .pipe(map(res => {
        console.log(res);
        return res;
      }));
  }

}
