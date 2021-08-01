import {Component} from '@angular/core';
import {SongsService} from "@services/songs.service";
import {Song} from "@app/models/Song";
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-songs',
  templateUrl: './songs.component.html',
  styleUrls: ['./songs.component.sass']
})
export class SongsComponent {

  songs: Song[] | undefined;

  faPen = faPen;
  faTrash = faTrash;

  constructor(
    private songService: SongsService,
  ) {
    this.getPlaylists();
  }

  getPlaylists() {
    this.songService.getSongs()
      .subscribe((data: Song[]) => {
        this.songs = data;
      });
  }

  deleteSong(songId: bigint) {
    console.log(`Se va a borrar la canción con ID ${songId}`);
  }

}
