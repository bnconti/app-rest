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
	/*
	for (playlist of data) {
	  newPlaylist: Playlist;
	  newPlaylist.name = playlist.name;
	  newPlaylist.email = playlist.user.email;
	  this.playlists.push(newPlaylist);
	}
	*/
       this.playlists = data;
      });
  }

}
