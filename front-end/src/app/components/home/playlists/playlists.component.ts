import { Component, OnInit } from '@angular/core';
import { PlaylistsService } from '@app/services/playlists.service';
import { Playlist } from '@app/models/Playlist';

@Component({
  selector: 'app-playlists',
  templateUrl: './playlists.component.html',
  styleUrls: ['./playlists.component.sass']
})
export class PlaylistsComponent implements OnInit {

  constructor(
    private playlistsService: PlaylistsService
  ) {
  }

  ngOnInit(): void {
    this.getPlaylists();
  }

  playlists: Playlist[] | undefined;

  getPlaylists() {
    this.playlistsService.getPlaylists()
      .subscribe((data: Playlist[]) => {
        this.playlists = data;
      });
  }

  delete() {
    alert("Para eliminar una lista, utilice el comando DELETE en la base de datos X>)");
  }
}
