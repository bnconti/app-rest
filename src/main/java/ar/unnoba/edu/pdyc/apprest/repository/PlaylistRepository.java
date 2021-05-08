package ar.unnoba.edu.pdyc.apprest.repository;

import ar.unnoba.edu.pdyc.apprest.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("playlistRepository")
public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
}
