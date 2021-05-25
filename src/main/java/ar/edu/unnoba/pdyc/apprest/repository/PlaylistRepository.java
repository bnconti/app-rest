package ar.edu.unnoba.pdyc.apprest.repository;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
    List<Playlist> findByUser(User user);
    Playlist findByUserAndName(User user, String name);
}
