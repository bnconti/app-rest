package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistServiceImp implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public List<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }
}
