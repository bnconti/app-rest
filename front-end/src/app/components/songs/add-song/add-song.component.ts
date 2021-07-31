import {Component} from '@angular/core';
import {faSave} from "@fortawesome/free-solid-svg-icons";
import {Genre} from "@app/models/Genre";
import {SongsService} from "@services/songs.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Song} from "@app/models/Song";

@Component({
  selector: 'app-add-song',
  templateUrl: './add-song.component.html',
  styleUrls: ['./add-song.component.sass']
})
export class AddSongComponent {

  newSongForm: FormGroup;
  genres: Genre[]

  faSave = faSave;

  loading = false;
  submitted = false;
  newSongError = false;
  newSongMsg = '';

  constructor(
    private songService: SongsService,
    private formBuilder: FormBuilder
) {
    this.newSongForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      author: ['', [Validators.required]],
      genre: [0]
    });

    this.genres = songService.getGenres();
  }

  get f() {
    return this.newSongForm.controls;
  }

  onSubmit(): void {

    this.submitted = true;

    if (this.newSongForm.invalid) {
      this.newSongError = true;
      this.newSongMsg = "Please verify the form errors";
      return;
    }

    this.loading = true;

    const name = this.newSongForm.controls['name'].value;
    const author = this.newSongForm.controls['author'].value;
    const genre = this.newSongForm.controls['genre'].value;

    if (this.songService.songExists(author, name)) {
      this.loading = false;
      this.newSongError = true;
      this.newSongMsg = "There is already a song with that name and author";
      return;
    }

    const newSong: Song = {name: name, author: author, genre: genre}

    this.songService.addSong(newSong)
      .subscribe({
        next: () => {
          this.newSongError = false;
          this.newSongMsg = "New song saved succesfully!";
        },
        error: () => {
          this.newSongError = true;
          this.newSongMsg = "Something went wrong while creating the new song.";
        },
        complete: () => {
          this.loading = false;
        }
      })
  }

}
