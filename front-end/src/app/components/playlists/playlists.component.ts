import {Component, ViewChild} from '@angular/core';
import {PlaylistsService} from '@app/services/playlists.service';
import {Playlist} from '@app/models/Playlist';
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmationDialogComponent} from "@app/components/confirmation-dialog/confirmation-dialog.component";
import {DialogData} from "@app/interfaces/DialogData";
import {NotificationService} from "@services/notification.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-playlists',
  templateUrl: './playlists.component.html',
  styleUrls: ['./playlists.component.sass']
})
export class PlaylistsComponent {

  playlistsDataSource: MatTableDataSource<Playlist> = new MatTableDataSource;
  displayedColumns: string[] = ['name', 'user', 'songs', 'edit', 'delete'];

  faPen = faPen;
  faTrash = faTrash;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private playlistsService: PlaylistsService,
    private router: Router,
    public dialog: MatDialog,
    private notification: NotificationService
  ) {
    this.getPlaylists();
  }

  getPlaylists() {
    this.playlistsService.getPlaylists()
      .subscribe((data: Playlist[]) => {
        this.playlistsDataSource = new MatTableDataSource(data);
        this.playlistsDataSource.paginator = this.paginator;
        this.playlistsDataSource.sort = this.sort;
      });
  }

  goToEditPlaylist(playlistId: string) {
    this.router.navigate([`home/playlists/${playlistId}`]);
  }

  deletePlaylistDialog(playlist: Playlist) {
    const dialogData: DialogData = {
      dialogTitle: 'MyMusic - Confirmation required',
      dialogMsg: 'Are you REALLY SURE you want to delete this playlist? There is no coming back',
      confirmationMsg: 'Yes, delete'
    }

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(accepted => {
      if (accepted) {
        this.deletePlaylist(playlist);
      }
    });
  }

  deletePlaylist(playlist: Playlist) {
    this.playlistsService.delete(playlist.id!).subscribe({
      next: (deleted) => {
        if (deleted) {
          // Actualizar la tabla quitando la playlist borrada
          const itemIndex = this.playlistsDataSource.data.findIndex(p => p === playlist);
          this.playlistsDataSource.data.splice(itemIndex, 1);
          this.playlistsDataSource.paginator = this.paginator;

          this.notification.success(`"${playlist.name}" deleted successfully`);
        } else {
          this.notification.error(`Something went wrong while deleting "${playlist.name}"`);
        }
      },
      error: (err) => {
        console.log(err);
        this.notification.error(`Something went wrong while deleting "${playlist.name}"`);
      }
    })
  }

  doFilter = (event: KeyboardEvent) => {
    const element = event.currentTarget as HTMLInputElement
    const value = element.value
    this.playlistsDataSource.filter = value.trim().toLocaleLowerCase();
  }
}
