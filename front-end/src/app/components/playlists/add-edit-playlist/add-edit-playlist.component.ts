import { Component, ViewChild } from '@angular/core';
import { faSave, faTrash, faSearch, faPlus } from "@fortawesome/free-solid-svg-icons";
import { Playlist } from "@app/models/Playlist";
import { Song } from "@app/models/Song";
import { Genre } from "@app/models/Genre";
import { PlaylistsService } from "@services/playlists.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
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
  styleUrls: ['./add-playlist.component.sass']
})
export class AddEditPlaylistComponent {

  isAddMode: boolean = true;
  playlistId: string;

  playlistForm: FormGroup;

  faSave = faSave;
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
    public dialog: MatDialog,
    private notification: NotificationService
  ) {
    this.playlistId = this.route.snapshot.params['id'];
    this.isAddMode = !this.playlistId;

    this.playlistForm = this.formBuilder.group({
      name: ['', [Validators.required]]
    });

    if (!this.isAddMode) {
      // Cargo los valores en caso de estar en modo edición
      this.playlistsService.getById(this.playlistId)
        .subscribe(
          (playlist: Playlist) => {
            this.playlistForm.patchValue(playlist);
            this.songsDataSource = new MatTableDataSource(playlist.songs);
            this.songsDataSource.paginator = this.paginator;
          },
          error => {
            this.notification.error("Something went wrong while retrieving the playlist.\nPerhaps the service is not running?");
          }
        );
    }
  }

  get f() {
    return this.playlistForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.playlistForm.invalid) {
      this.notification.error("Please verify the form errors.");
      return;
    }

    this.loading = true;

    const name = this.playlistForm.controls['name'].value.trim();
    //const userEmail = JSON.parse(sessionStorage.getItem("currentUser")!).email;

    if (!this.isAddMode) {
      this.renamePlaylist(name);
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
          this.notification.success("New playlist saved successfully!");
          // Redirigir a la página anterior
          window.history.back();
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while creating the new playlist.");
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

  renamePlaylist(newName: string) {
    this.playlistsService.rename(this.playlistId, newName)
      .subscribe({
        next: () => {
          this.notification.success("Playlist updated successfully!");
          window.history.back();
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while renaming the playlist.");
        },
        complete: () => {
          this.loading = false;
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
    /* TODO
    this.songService.delete(song.id!).subscribe({
      next: (deleted) => {
        if (deleted) {
          // Actualizar la tabla quitando la canción borrada
          const itemIndex = this.songsDataSource.data.findIndex(s => s === song);
          this.songsDataSource.data.splice(itemIndex, 1);
          this.songsDataSource.paginator = this.paginator;

          this.notification.success(`"${song.author} - ${song.name}" deleted successfully`);
        } else {
          this.notification.error(`Can't delete "${song.author} - ${song.name}" because it's included in one or more playlists`);
        }
      },
      error: (err) => {
        console.log(err);
        this.notification.error(`Something went wrong while deleting "${song.author} - ${song.name}"`);
      }
    })
    */
  }

  doFilter = (event: KeyboardEvent) => {
    const element = event.currentTarget as HTMLInputElement;
    const value = element.value;
    this.songsDataSource.filter = value.trim().toLocaleLowerCase();
  }


}
