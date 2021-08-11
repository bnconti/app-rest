import { Component } from '@angular/core';
import { faSave } from "@fortawesome/free-solid-svg-icons";
import { Genre } from "@app/models/Genre";
import { SongsService } from "@services/songs.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Song } from "@app/models/Song";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { NotificationService } from "@services/notification.service";

@Component({
  selector: 'app-add-edit-song',
  templateUrl: './add-edit-song.component.html',
  styleUrls: ['./add-song.component.sass']
})
export class AddEditSongComponent {

  isAddMode: boolean = true;
  songId: string;

  songForm: FormGroup;
  genres: Genre[];
  authors: String[] | undefined;
  filteredAuthors: Observable<String[]> | undefined;

  faSave = faSave;

  loading = false;
  submitted = false;

  constructor(
    private songService: SongsService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private notification: NotificationService
  ) {
    this.songId = this.route.snapshot.params['id'];
    this.isAddMode = !this.songId;

    this.songForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      author: ['', [Validators.required]],
      genre: [0]
    });

    this.genres = songService.getGenres();
    this.getAuthors();
    this.filterAuthors();

    if (!this.isAddMode) {
      // Cargo los valores en caso de estar en modo edición
      this.songService.getById(this.songId)
        .subscribe(
          (song: Song) => {
            this.songForm.patchValue(song);

            // Al género hay que definirlo a mano porque traemos un string en vez del ID
            const genre = this.genres.find(genre => genre.desc == song.genre);
            this.songForm.controls['genre'].patchValue(genre!.id);
          },
          error => {
            this.notification.error("Something went wrong while retrieving the song.\nPerhaps the service is not running?");
          }
        );
    }
  }

  getAuthors() {
    this.songService.getAuthors()
      .subscribe(
        (authors: String[]) => {
          this.authors = authors;
        },
        error => {
          this.notification.error("Something went wrong while retrieving the authors.\nPerhaps the service is not running?");
        }
      );
  }

  filterAuthors() {
    // @ts-ignore
    this.filteredAuthors = this.songForm.controls['author'].valueChanges
      .pipe(
        map(value => this._filter(value)),
        startWith(null)
      );
  }

  private _filter(value: string): String[] {
    const filterValue = value.toLowerCase();
    return this.authors!.filter(author => author.toLowerCase().includes(filterValue));
  }

  get f() {
    return this.songForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.songForm.invalid) {
      this.notification.error("Please verify the form errors.");
      return;
    }

    this.loading = true;

    const name = this.songForm.controls['name'].value.trim();
    const author = this.songForm.controls['author'].value.trim();
    const genre = this.songForm.controls['genre'].value;

    // FIXME Corregir actualizaciones
    this.songService.existsByAuthorAndName(author, name)
      .subscribe({
        next: (exists) => {
          if (exists) {
            this.loading = false;
            this.notification.error("There is already a song with that name and author.");
          } else {
            const song: Song = {id: this.songId, name: name, author: author, genre: genre};
            this.isAddMode ? this.createSong(song) : this.updateSong(song);
          }
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while creating the new song.\nPerhaps the service is not running?");
        }
      });
  }

  createSong(newSong: Song) {
    this.songService.add(newSong)
      .subscribe({
        next: () => {
          this.notification.success("New song saved successfully!");
          // Redirigir a la página anterior
          window.history.back();
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while creating the new song.");
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

  updateSong(updatedSong: Song) {
    this.songService.update(updatedSong)
      .subscribe({
        next: () => {
          this.notification.success("Song updated successfully!");
          window.history.back();
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while updating the song.");
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

}
