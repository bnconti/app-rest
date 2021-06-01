package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;

import java.util.List;

public interface SongService {
    List<Song> getSongs();

    List<Song> getSongsByAuthor(String author);

    List<Song> getSongsByGenre(Genre genre);
    List<Song> getSongsByGenre(String strGenre);

    Song getSongById(Long id);

    List<Song> getSongsByAuthorAndGenre(String author, Genre genre);
    List<Song> getSongsByAuthorAndGenre(String author, String strGenre);

    List<Song> getSongsByName(String name);

    List<Song> getSongsByAuthorAndName(String author, String name);

    List<Song> getSongsByPlaylist(Playlist name);
}
