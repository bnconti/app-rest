package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SongService {
    CompletableFuture<List<Song>> getSongs();

    CompletableFuture<List<Song>> getSongsByAuthor(String author);

    CompletableFuture<List<Song>> getSongsByGenre(Genre genre);

    CompletableFuture<List<Song>> getSongsByGenre(String strGenre);

    CompletableFuture<Song> getSongById(Long id);

    CompletableFuture<List<Song>> getSongsByAuthorAndGenre(String author, Genre genre);

    CompletableFuture<List<Song>> getSongsByAuthorAndGenre(String author, String strGenre);

    CompletableFuture<List<Song>> getSongsByName(String name);

    CompletableFuture<List<Song>> getSongsByAuthorAndName(String author, String name);

    CompletableFuture<List<Song>> getSongsByPlaylist(Playlist name);
}
