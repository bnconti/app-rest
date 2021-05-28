package ar.edu.unnoba.pdyc.apprest.repository;

import ar.edu.unnoba.pdyc.apprest.model.Genre;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("songRepository")
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAll();

    List<Song> findByAuthor(String author);

    List<Song> findByName(String name);

    List<Song> findByAuthorAndName(String author, String name);

    List<Song> findByAuthorAndGenre(String author, String genre);

    List<Song> findByGenre(Genre genre);
}
