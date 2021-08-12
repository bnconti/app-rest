package ar.edu.unnoba.pdyc.apprest.repository;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("playlistsRepository")
public interface PlaylistsRepository extends JpaRepository<Playlist, Long> {
    boolean existsById(Long id);
    boolean existsBySongs(Song song);

    List<Playlist> findAll();
    List<Playlist> findByUser(User user);
    Playlist findByUserAndName(User user, String name);
}