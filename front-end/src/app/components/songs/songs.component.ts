import {Component} from '@angular/core';
import {SongsService} from "@services/songs.service";
import {Song} from "@app/models/Song";
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {Router} from "@angular/router";

@Component({
  selector: 'app-songs',
  templateUrl: './songs.component.html',
  styleUrls: ['./songs.component.sass']
})
export class SongsComponent {

  songs: Song[] | undefined;

  faPen = faPen;
  faTrash = faTrash;

  err: boolean = false;
  showMsg: boolean = false;
  msg: String = '';

  constructor(
    private songService: SongsService,
    private router: Router
  ) {
    this.getPlaylists();
  }

  getPlaylists() {
    this.songService.getSongs()
      .subscribe((data: Song[]) => {
        this.songs = data;
      });
  }

  goToEditSong(songId: string) {
    this.router.navigate([`home/songs/${songId}`]);
  }

  deleteSong(song: Song) {
    this.songService.delete(song.id!).subscribe({
      next: (deleted) => {
        this.showMsg = true;
        if (deleted) {
          this.songs = this.songs!.filter((s: Song) => s.id != song.id)
          this.err = false;
          this.msg = `"${song.author} - ${song.name}" removed successfully`;
        } else {
          this.err = true;
          this.msg = `Can't delete "${song.author} - ${song.name}" because it's included in one or more playlists`;
        }
      },
      error: (err) => {
        console.log(err);
        this.err = true;
        this.msg = `Something went wrong while removing "${song.author} - ${song.name}"`;
    }
    })
  }

}
