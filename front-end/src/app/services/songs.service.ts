import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {Song} from "@app/models/Song";
import {environment} from "@environments/environment";
import {map} from "rxjs/operators";

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

  addSong(newSong: Song): Observable<Song> {
    return this.http.post<Song>(SongsService.url, newSong)
      .pipe(map(res => {
        return res;
      }));
  }

}
