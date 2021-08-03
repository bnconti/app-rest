import {Component} from '@angular/core';
import {faSave} from "@fortawesome/free-solid-svg-icons";
import {Genre} from "@app/models/Genre";
import {SongsService} from "@services/songs.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Song} from "@app/models/Song";
import {ActivatedRoute} from "@angular/router";

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

  faSave = faSave;

  loading = false;
  submitted = false;
  err = false;
  msg = '';

  constructor(
    private songService: SongsService,
    private formBuilder: FormBuilder,
    private route: ActivatedRoute
  ) {
    this.songId = this.route.snapshot.params['id'];
    this.isAddMode = !this.songId;

    this.songForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      author: ['', [Validators.required]],
      genre: [0]
    });

    this.genres = songService.getGenres();

    if (!this.isAddMode) {
      // Cargo los valores en caso de estar en modo edición
      this.songService.getById(this.songId)
        .subscribe(song => {
          this.songForm.patchValue(song);

          // El genre hay que definirlo a mano porque trae un string en vez del ID
          const genre = this.genres.find(genre => genre.desc == song.genre);
          this.songForm.controls['genre'].patchValue(genre!.id);
        });
    }
  }

  get f() {
    return this.songForm.controls;
  }

  onSubmit(): void {

    this.submitted = true;

    if (this.songForm.invalid) {
      this.err = true;
      this.msg = "Please verify the form errors";
      return;
    }

    this.loading = true;

    const name = this.songForm.controls['name'].value;
    const author = this.songForm.controls['author'].value;
    const genre = this.songForm.controls['genre'].value;

    this.songService.existsByAuthorAndName(author, name)
      .subscribe({
        next: (exists) => {
          if (exists) {
            this.loading = false;
            this.err = true;
            this.msg = "There is already a song with that name and author";
          } else {
            const song: Song = {id: this.songId,name: name, author: author, genre: genre};
            this.isAddMode ? this.createSong(song) : this.updateSong(song);
          }
        }
      });
  }

  createSong(newSong: Song) {
    this.songService.add(newSong)
      .subscribe({
        next: () => {
          this.err = false;
          this.msg = "New song saved succesfully!";
        },
        error: () => {
          this.err = true;
          this.msg = "Something went wrong while creating the new song.";
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
          this.err = false;
          this.msg = "Song updated succesfully!";
        },
        error: () => {
          this.err = true;
          this.msg = "Something went wrong while updating the new song.";
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

}