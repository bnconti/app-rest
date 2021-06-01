package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SongServiceImp implements SongService {
    @Autowired
    private SongRepository songRepository;

    @Override
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

    @Override
    public Song getSongById(Long id) {
        return songRepository.findSongById(id);
    }

    @Override
    public List<Song> getSongsByAuthor(String author) {
        if (author == null) {
            return getSongs();
        } else {
            return songRepository.findByAuthor(author);
        }
    }

    @Override
    public List<Song> getSongsByGenre(Genre genre) {
        if (genre == null) {
            return getSongs();
        } else {
            return songRepository.findByGenre(genre);
        }
    }
    @Override
    public List<Song> getSongsByGenre(String strGenre) {
        if (strGenre == null) {
            return getSongs();
        }
        try {
            Genre genre = Genre.valueOf(strGenre.toUpperCase());
            return getSongsByGenre(genre);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Song> getSongsByAuthorAndGenre(String author, Genre genre) {
        if (author == null) {
            return getSongsByGenre(genre);
        } else if (genre == null) {
            return getSongsByAuthor(author);
        } else {
            return songRepository.findByAuthorAndGenre(author, genre);
        }
    }
    @Override
    public List<Song> getSongsByAuthorAndGenre(String author, String strGenre) {
        if (strGenre == null) {
            return getSongsByAuthor(author);
        }
        try {
            Genre genre = Genre.valueOf(strGenre.toUpperCase());
            return getSongsByAuthorAndGenre(author, genre);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Song> getSongsByName(String name) {
        return songRepository.findByName(name);
    }

    @Override
    public List<Song> getSongsByAuthorAndName(String author, String name) {
        return songRepository.findByAuthorAndName(author, name);
    }

    @Override
    public List<Song> getSongsByPlaylist(Playlist name) {
        return songRepository.findByPlaylists(name);
    }
}
