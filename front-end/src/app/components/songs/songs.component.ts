import {Component, ViewChild} from '@angular/core';
import {SongsService} from "@services/songs.service";
import {Song} from "@app/models/Song";
import {faPen, faTrash, faSearch} from '@fortawesome/free-solid-svg-icons';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "@app/components/confirmation-dialog/confirmation-dialog.component";
import {DialogData} from "@app/interfaces/DialogData";
import {NotificationService} from "@services/notification.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-songs',
  templateUrl: './songs.component.html',
  styleUrls: ['./songs.component.sass']
})
export class SongsComponent {

  songsDataSource: MatTableDataSource<Song> = new MatTableDataSource;
  displayedColumns: string[] = ['author', 'name', 'genre', 'edit', 'delete'];

  faPen = faPen;
  faTrash = faTrash;
  faSearch = faSearch;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private songService: SongsService,
    private router: Router,
    public dialog: MatDialog,
    private notification: NotificationService
  ) {
    this.getSongs();
  }

  getSongs() {
    this.songService.getSongs()
      .subscribe(
        (data: Song[]) => {
          this.songsDataSource = new MatTableDataSource(data);
          this.songsDataSource.paginator = this.paginator;
          this.songsDataSource.sort = this.sort;
        },
        error => {
          this.notification.error("This is embarrasing...\nSomething went wrong while retrieving the songs list.\nPerhaps the service is not running?");
        }
      );
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
  }

  doFilter = (event: KeyboardEvent) => {
    const element = event.currentTarget as HTMLInputElement
    const value = element.value
    this.songsDataSource.filter = value.trim().toLocaleLowerCase();
  }

}
