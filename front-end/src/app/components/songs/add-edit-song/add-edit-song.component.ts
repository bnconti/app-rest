import { Component } from '@angular/core';
import { faSave, faBackward } from "@fortawesome/free-solid-svg-icons";
import { Genre } from "@app/models/Genre";
import { SongsService } from "@services/songs.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Song } from "@app/models/Song";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable } from "rxjs";
import { map, startWith } from "rxjs/operators";
import { NotificationService } from "@services/notification.service";

@Component({
  selector: 'app-add-edit-song',
  templateUrl: './add-edit-song.component.html',
  styleUrls: ['./add-edit-song.component.sass']
})
export class AddEditSongComponent {

  isAddMode: boolean = true;
  songId: string;

  songForm: FormGroup;
  genres: Genre[];
  authors: string[] | undefined;
  filteredAuthors: Observable<string[]> | undefined;

  faSave = faSave;
  faBack = faBackward;

  loading = false;
  submitted = false;

  constructor(
    private songService: SongsService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
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
            this.goToSongsList();
          }
        );
    }
  }

  getAuthors() {
    this.songService.getAuthors()
      .subscribe(
        (authors: string[]) => {
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

  private _filter(value: string): string[] {
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

    this.songService.getIdByAuthorAndName(author, name)
      .subscribe({
        next: (existingId: string) => {
          this.loading = false;

          // Si existe una canción con el mismo autor y nombre y se está creando, mostrar error.
          // Si se está actualizando, ver si la que se encontró es una distinta.
          if ( existingId != null && (this.isAddMode || this.songId != existingId) ) {
            this.notification.error("There is already a song with that name and author.");
          } else {
            // Si se crea una canción nueva, songId es indefinido, pero el back-end
            // siempre crea un Id automáticamente.
            const song: Song = {id: this.songId, name: name, author: author, genre: genre};
            this.isAddMode ? this.createSong(song) : this.updateSong(song);
          }
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while verifying if there were any conflicts.\nPerhaps the service is not running?");
        }
      });
  }

  createSong(newSong: Song) {
    this.songService.add(newSong)
      .subscribe({
        next: () => {
          this.loading = false;
          this.notification.success("Song created successfully!");
          this.goToSongsList();
        },
        error: () => {
          this.notification.error("Something went wrong while creating the song.");
        }
      })
  }

  updateSong(updatedSong: Song) {
    this.songService.update(updatedSong)
      .subscribe({
        next: () => {
          this.loading = false;
          this.notification.success("Song updated successfully!");
          this.goToSongsList();
        },
        error: () => {
          this.loading = false;
          this.notification.error("Something went wrong while updating the song.");
        }
      })
  }

  goToSongsList() {
    this.router.navigate(["home/songs"]);
  }

}
