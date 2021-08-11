package ar.edu.unnoba.pdyc.apprest.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Song;

public interface SongService {
    /* variantes sincrónicas */

    Boolean existsByAuthorAndName(String author, String name);

    List<Song> getSongs();
    List<String> getAuthors();

    List<Song> getSongsByAuthor(String author);

    List<Song> getSongsByGenre(Genre genre);
    List<Song> getSongsByGenre(String strGenre);

    Song getSongById(Long id);

    List<Song> getSongsByAuthorAndGenre(String author, Genre genre);
    List<Song> getSongsByAuthorAndGenre(String author, String strGenre);

    List<Song> getSongsByName(String name);

    Song getSongByAuthorAndName(String author, String name);

    /*
    List<Song> getSongsByPlaylist(Playlist name);
    */

    Song create(Song newSong);
    Song update(Song updatedSong);
    Boolean delete(Long id);

    /* variantes asincrónicas */

    CompletableFuture<Boolean> existsByAuthorAndNameAsync(String author, String name);

    CompletableFuture<List<Song>> getSongsAsync();
    CompletableFuture<List<String>> getAuthorsAsync();

    CompletableFuture<List<Song>> getSongsByAuthorAsync(String author);

    CompletableFuture<List<Song>> getSongsByGenreAsync(Genre genre);
    CompletableFuture<List<Song>> getSongsByGenreAsync(String strGenre);

    CompletableFuture<Song> getSongByIdAsync(Long id);

    CompletableFuture<List<Song>> getSongsByAuthorAndGenreAsync(String author, Genre genre);
    CompletableFuture<List<Song>> getSongsByAuthorAndGenreAsync(String author, String strGenre);

    CompletableFuture<List<Song>> getSongsByNameAsync(String name);

    CompletableFuture<Song> getSongByAuthorAndNameAsync(String author, String name);

    /*
    CompletableFuture<List<Song>> getSongsByPlaylistAsync(Playlist name);
    */

    CompletableFuture<Song> createAsync(Song newSong);
    CompletableFuture<Song> updateAsync(Song updatedSong);
    CompletableFuture<Boolean> deleteAsync(Long id);
}
