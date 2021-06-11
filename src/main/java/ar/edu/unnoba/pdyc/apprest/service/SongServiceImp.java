package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class SongServiceImp implements SongService {
    @Autowired
    private SongRepository songRepository;

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongs() {
        List<Song> songs = songRepository.findAll();
        return CompletableFuture.completedFuture(songs);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> getSongById(Long id) {
        Song song = songRepository.findSongById(id);
        return CompletableFuture.completedFuture(song);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthor(String author) {
        if (author == null) {
            return getSongs();
        }
        List<Song> songs = songRepository.findByAuthor(author);
        return CompletableFuture.completedFuture(songs);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByGenre(Genre genre) {
        if (genre == null) {
            return getSongs();
        }
        return CompletableFuture.completedFuture(songRepository.findByGenre(genre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByGenre(String strGenre) {
        if (strGenre == null) {
            return getSongs();
        }
        try {
            Genre genre = Genre.valueOf(strGenre.toUpperCase());
            return getSongsByGenre(genre);
        } catch (IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAndGenre(String author, Genre genre) {
        if (author == null) {
            return getSongsByGenre(genre);
        }
        if (genre == null) {
            return getSongsByAuthor(author);
        } else {
            List<Song> songs = songRepository.findByAuthorAndGenre(author, genre);
            return CompletableFuture.completedFuture(songs);
        }
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAndGenre(String author, String strGenre) {
        if (strGenre == null) {
            return getSongsByAuthor(author);
        }
        try {
            Genre genre = Genre.valueOf(strGenre.toUpperCase());
            return getSongsByAuthorAndGenre(author, genre);
        } catch (IllegalArgumentException e) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByName(String name) {
        return CompletableFuture.completedFuture(songRepository.findByName(name));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAndName(String author, String name) {
        return CompletableFuture.completedFuture(songRepository.findByAuthorAndName(author, name));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByPlaylist(Playlist name) {
        return CompletableFuture.completedFuture(songRepository.findByPlaylists(name));
    }
}
