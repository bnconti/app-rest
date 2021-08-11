import { Component } from '@angular/core';
import { faSave } from "@fortawesome/free-solid-svg-icons";
import { Playlist } from "@app/models/Playlist";
import { Song } from "@app/models/Song";
import { Genre } from "@app/models/Genre";
import { PlaylistsService } from "@services/playlists.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { NotificationService } from "@services/notification.service";

@Component({
  selector: 'app-add-edit-playlist',
  templateUrl: './add-edit-playlist.component.html',
  styleUrls: ['./add-playlist.component.sass']
})
export class AddEditPlaylistComponent {

  isAddMode: boolean = true;
  playlistId: string;
  user: string | undefined;
  songs: Song[] | undefined;

  playlistForm: FormGroup;

  faSave = faSave;

  loading = false;
  submitted = false;

  constructor(
    private playlistsService: PlaylistsService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
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
          playlist => {
            this.playlistForm.patchValue(playlist);
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

    const name = this.playlistForm.controls['name'].value;
    // No hace falta mostrar el usuario porque va a ser siempre el logueado
    const user = this.playlistForm.controls['user'].value;
    // TODO: Esto ni idea...
    const songs = this.playlistForm.controls['songs'].value;

    this.playlistsService.existsByName(name)
      .subscribe({
        next: (exists) => {
          if (exists) {
            this.loading = false;
            this.notification.error("There is already a playlist with that name.");
          } else {
            const playlist: Playlist = {id: this.playlistId, name: name, user: user, songs: songs};
            this.isAddMode ? this.createPlaylist(playlist) : this.updatePlaylist(playlist);
          }
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while creating the new playlist.\nPerhaps the service is not running?");
        }
      });
  }

  createPlaylist(newPlaylist: Playlist) {
    this.playlistsService.add(newPlaylist)
      .subscribe({
        next: () => {
          this.notification.success("New playlist saved successfully!");
        },
        error: () => {
          this.notification.error("Something went wrong while creating the new playlist.");
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

  updatePlaylist(updatedPlaylist: Playlist) {
    this.playlistsService.update(updatedPlaylist)
      .subscribe({
        next: () => {
          this.notification.success("Playlist updated successfully!");
        },
        error: () => {
          this.notification.error("Something went wrong while updating the playlist.");
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

}
