package ar.unnoba.edu.pdyc.apprest.service;

import ar.unnoba.edu.pdyc.apprest.model.Song;
import ar.unnoba.edu.pdyc.apprest.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImp implements SongService {
    @Autowired
    private SongRepository songRepository;

    @Override
    public List<Song> getSongs() {
        return songRepository.findAll();
    }
}
