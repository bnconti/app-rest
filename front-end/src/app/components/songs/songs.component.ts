import {Component, OnInit} from '@angular/core';
import {SongsService} from "@services/songs.service";
import {Song} from "@app/models/Song";
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-songs',
  templateUrl: './songs.component.html',
  styleUrls: ['./songs.component.sass']
})
export class SongsComponent implements OnInit {

  songs: Song[] | undefined;

  faPen = faPen;
  faTrash = faTrash;

  constructor(
    private songService: SongsService
  ) {
  }

  ngOnInit(): void {
    this.getPlaylists()
  }

  getPlaylists() {
    this.songService.getSongs()
      .subscribe((data: Song[]) => {
        this.songs = data;
      });
  }

}
