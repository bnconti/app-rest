package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Song;

import java.util.List;

public interface SongService {
    List<Song> getSongs();
    List<Song> getSongs(String author, Genre genre);

    List<Song> getSongs(String author, String genre);
}
