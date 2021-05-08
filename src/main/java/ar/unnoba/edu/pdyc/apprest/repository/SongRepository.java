package ar.unnoba.edu.pdyc.apprest.repository;

import ar.unnoba.edu.pdyc.apprest.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("songRepository")
public interface SongRepository extends JpaRepository<Song,Long> {

}
