import {Component} from '@angular/core';
import {SongsService} from "@services/songs.service";
import {Song} from "@app/models/Song";
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "@app/components/confirmation-dialog/confirmation-dialog.component";
import {DialogData} from "@app/interfaces/DialogData";
import {NotificationService} from "@services/notification.service";

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
    private router: Router,
    public dialog: MatDialog,
    private notification: NotificationService
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

  deleteSongDialog(song: Song) {
    const dialogData: DialogData = {
      dialogTitle: 'MyMusic - Confirmation required',
      dialogMsg: 'Are you REALLY SURE you want to delete this song? There is no coming back',
      confirmationMsg: 'Yes, delete'
    }

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(accepted => {
      if (accepted) {
        this.deleteSong(song);
      }
    });
  }

  deleteSong(song: Song) {
    this.songService.delete(song.id!).subscribe({
      next: (deleted) => {
        if (deleted) {
          this.songs = this.songs!.filter((s: Song) => s.id != song.id)
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
  }

}
