import { Component, ViewChild } from '@angular/core';
import { PlaylistsService } from "@services/playlists.service";
import { SongsService } from "@services/songs.service";
import { Song } from "@app/models/Song";
import { faSearch, faPlus, faBackward } from '@fortawesome/free-solid-svg-icons';
import { ActivatedRoute, Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { NotificationService } from "@services/notification.service";
import { MatTableDataSource } from "@angular/material/table";
import { MatSort } from "@angular/material/sort";
import { MatPaginator } from "@angular/material/paginator";
import { SelectionModel } from "@angular/cdk/collections";

@Component({
  selector: 'app-songs',
  templateUrl: './add-playlist-song.component.html',
  styleUrls: ['./add-playlist-song.component.sass']
})
export class AddPlaylistSongComponent {

  playlistId: string;

  songsDataSource: MatTableDataSource<Song> = new MatTableDataSource;
  displayedColumns: string[] = ['author', 'name', 'genre', 'add'];
  // Crear modelo para poder seleccionar múltiples filas, sin selección predeterminada.
  selection = new SelectionModel<Song>(true, []);

  faSearch = faSearch;
  faPlus = faPlus;
  faBack = faBackward;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private playlistsService: PlaylistsService,
    private songService: SongsService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private notification: NotificationService
  ) {
    this.playlistId = this.route.snapshot.params['id'];

    this.getSongs();
  }

  getSongs() {
    // Mostrar todas las canciones cargadas en el sistema.
    // Lo ideal sería mostrar sólo las canciones que no están en esta playlist...
    this.songService.getSongs()
      .subscribe(
        (data: Song[]) => {
          this.songsDataSource = new MatTableDataSource(data);
          this.songsDataSource.paginator = this.paginator;
          this.songsDataSource.sort = this.sort;
        },
        error => {
          this.notification.error("This is embarrasing...\nSomething went wrong while retrieving the songs.\nPerhaps the service is not running?");
        }
      );
  }

  addSongToPlaylist(song: Song) {
    if (this.selection.isSelected(song)) {
      this.notification.error("You have already added that song.");
      return;
    }

    this.playlistsService.addSong(this.playlistId, song.id!)
      .subscribe(
	next => {
	  this.notification.success("Song added successfully to the playlist!");
          // Indicar qué canción se agregó seleccionando la fila
          this.selection.select(song);
	},
	error => {
          if (error == "Unknown Error") {
            this.notification.error("Something went wrong while adding the song to the playlist.\nPerhaps the service is not running?");
	  } else {
            // Ya estaba de antes en la lista
            this.notification.error("You have already added that song to the playlist.");
            this.selection.select(song);
	  }
	}
      );
  }

  doFilter = (event: KeyboardEvent) => {
    const element = event.currentTarget as HTMLInputElement;
    const value = element.value;
    this.songsDataSource.filter = value.trim().toLocaleLowerCase();
  }

  goToPlaylistsList() {
    this.router.navigate([`home/playlists/${this.playlistId}`]);
  }

}
