import { Component, ViewChild } from '@angular/core';
import { faSave, faBackward, faTrash, faSearch, faPlus } from "@fortawesome/free-solid-svg-icons";
import { Playlist } from "@app/models/Playlist";
import { Song } from "@app/models/Song";
import { Genre } from "@app/models/Genre";
import { PlaylistsService } from "@services/playlists.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable } from "rxjs";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmationDialogComponent } from "@app/components/confirmation-dialog/confirmation-dialog.component";
import { DialogData } from "@app/interfaces/DialogData";
import { NotificationService } from "@services/notification.service";

// Para la tabla de canciones
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-add-edit-playlist',
  templateUrl: './add-edit-playlist.component.html',
  styleUrls: ['./add-edit-playlist.component.sass']
})
export class AddEditPlaylistComponent {

  isAddMode: boolean = true;
  playlistId: string;
  playlistName: string | undefined;
  loggedUser: string;

  playlistForm: FormGroup;

  faSave = faSave;
  faBack = faBackward;
  faTrash = faTrash;
  faSearch = faSearch;
  faPlus = faPlus;

  loading = false;
  submitted = false;

  songsDataSource: MatTableDataSource<Song> = new MatTableDataSource;
  displayedColumns: string[] = ['number', 'author', 'name', 'genre', 'remove'];


  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private playlistsService: PlaylistsService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private notification: NotificationService
  ) {
    this.playlistId = this.route.snapshot.params['id'];
    this.isAddMode = !this.playlistId;
    this.loggedUser = JSON.parse(sessionStorage.getItem("currentUser")!).email;

    this.playlistForm = this.formBuilder.group({
      name: ['', [Validators.required]]
    });

    if (!this.isAddMode) {
      // Cargo los valores en caso de estar en modo edición
      this.playlistsService.getById(this.playlistId)
        .subscribe(
          (playlist: Playlist) => {
            // Fijarse si coincide el usuario actual con el que creó la lista
            if (this.loggedUser != playlist.user.email) {
              // No coincide, probablemente el usuario ingresó la URL manualmente...
              // Enviarlo al listado de playlists
              this.router.navigate(["home/playlists"]);
            } else {
              this.playlistForm.patchValue(playlist);
              this.playlistName = playlist.name;
              this.songsDataSource = new MatTableDataSource(playlist.songs);
              this.songsDataSource.paginator = this.paginator;
            }
          },
          error => {
            // No se pudo encontrar la lista (por ejemplo, porque se ingresó
            // una URL con un ID inexistente).
            this.router.navigate(["home/playlists"]);
          }
        );
    }
  }

  get f() {
    return this.playlistForm.controls;
  }

  // Botón Rename
  onSubmit(): void {
    this.submitted = true;

    if (this.playlistForm.invalid) {
      this.notification.error("Please verify the form errors.");
      return;
    }

    this.loading = true;

    const newName = this.playlistForm.controls['name'].value.trim();

    if (!this.isAddMode) {
      this.renamePlaylist(newName);
    }

    /* TODO
    this.playlistsService.getByName(name)
      .subscribe({
        next: (existingPlaylist: Playlist) => {
          if (existingPlaylist && (this.isAddMode || this.playlistId != existingPlaylist.id)) {
            this.loading = false;
            this.notification.error("There is already a playlist with that name.");
          } else {
            const playlist: Playlist = {id: this.playlistId, name: name, user: this.user, songs: this.songs};
            this.isAddMode ? this.createPlaylist(playlist) : this.updatePlaylist(playlist);
          }
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while creating the new playlist.\nPerhaps the service is not running?");
        }
      });
    }
    */
  }

  createPlaylist(newPlaylist: Playlist) {
    this.playlistsService.add(newPlaylist)
      .subscribe({
        next: () => {
          this.loading = false;
          this.notification.success("New playlist saved successfully!");
          // Redirigir a la página anterior
          window.history.back();
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while creating the new playlist.");
        }
      })
  }

  renamePlaylist(newName: string) {
    // Verificar si es necesario renombrar
    if (this.playlistName == newName) {
      this.loading = false;
      this.notification.error("You did not change the playlist name.");
      return;
    }

    this.playlistsService.rename(this.playlistId, newName)
      .subscribe({
        next: () => {
          this.loading = false;
          this.notification.success("Playlist renamed successfully!");
          this.playlistName = newName;
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while renaming the playlist.");
        }
      })
  }

  removeSongDialog(song: Song) {
    const dialogData: DialogData = {
      dialogTitle: 'MyMusic - Confirmation required',
      dialogMsg: 'Are you sure you want to remove the song "'
                 + song.name + '" from this playlist?',
      confirmationMsg: 'Yes, remove'
    }

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(accepted => {
      if (accepted) {
        this.removeSong(song);
      }
    });
  }

  removeSong(song: Song) {
    this.playlistsService.removeSong(this.playlistId, song.id!).subscribe({
      next: () => {
        // Actualizar la tabla quitando la canción borrada
        const itemIndex = this.songsDataSource.data.findIndex(s => s === song);
        this.songsDataSource.data.splice(itemIndex, 1);
        this.songsDataSource.paginator = this.paginator;

        this.notification.success(`"${song.author} - ${song.name}" removed successfully.`);
      },
      error: (err) => {
        console.log(err);
        this.notification.error(`Something went wrong while removing "${song.author} - ${song.name}".`);
      }
    })
  }

  doFilter = (event: KeyboardEvent) => {
    const element = event.currentTarget as HTMLInputElement;
    const value = element.value;
    this.songsDataSource.filter = value.trim().toLocaleLowerCase();
  }

}
