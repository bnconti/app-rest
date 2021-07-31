package ar.edu.unnoba.pdyc.apprest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.repository.SongRepository;

@Service
public class SongServiceImp implements SongService {
    @Autowired
    private SongRepository songRepository;

    /*** variantes sincrónicas ***/

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

    @Override
    public Song create(Song newSong) {
        return songRepository.save(newSong);
    }

    @Override
    public Song update(Song updatedSong) {
        return songRepository.save(updatedSong);
    }

    @Override
    public Boolean delete(Long id) {
        Optional<Song> song = songRepository.findById(id);

        if (song.isEmpty()) {
            return false;
        } else {
            songRepository.delete(song.get());
            return true;
        }
    }

    @Override
    public Boolean existsByAuthorAndName(String author, String name) {
        return songRepository.existsByAuthorAndName(author, name);
    }

    /*** variantes asincrónicas - llaman a las funciones sincrónicas definidas arriba ***/

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsAsync() {
        return CompletableFuture.completedFuture(getSongs());
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> getSongByIdAsync(Long id) {
        return CompletableFuture.completedFuture(getSongById(id));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAsync(String author) {
        return CompletableFuture.completedFuture(getSongsByAuthor(author));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByGenreAsync(Genre genre) {
        return CompletableFuture.completedFuture(getSongsByGenre(genre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByGenreAsync(String strGenre) {
        return CompletableFuture.completedFuture(getSongsByGenre(strGenre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAndGenreAsync(String author, Genre genre) {
        return CompletableFuture.completedFuture(getSongsByAuthorAndGenre(author, genre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAndGenreAsync(String author, String strGenre) {
        return CompletableFuture.completedFuture(getSongsByAuthorAndGenre(author, strGenre));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByNameAsync(String name) {
        return CompletableFuture.completedFuture(getSongsByName(name));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByAuthorAndNameAsync(String author, String name) {
        return CompletableFuture.completedFuture(getSongsByAuthorAndName(author, name));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Song>> getSongsByPlaylistAsync(Playlist name) {
        return CompletableFuture.completedFuture(getSongsByPlaylist(name));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> createAsync(Song newSong) {
        return CompletableFuture.completedFuture(create(newSong));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Song> updateAsync(Song updatedSong) {
        return CompletableFuture.completedFuture(update(updatedSong));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> deleteAsync(Long id) {
        return CompletableFuture.completedFuture(delete(id));
    }

    @Override
    public CompletableFuture<Boolean> existsByAuthorAndNameAsync(String author, String name) {
        return CompletableFuture.completedFuture(existsByAuthorAndName(author, name));
    }
}
