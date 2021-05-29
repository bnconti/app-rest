package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImp implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public List<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }
    @Override
    public List<Playlist> getPlaylistsByUser(User user) {
        return playlistRepository.findByUser(user);
    }

    @Override
    public Playlist getPlaylistById(Long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        return (playlist.isEmpty() ? null : playlist.get());
    }
    @Override
    public Playlist getPlaylistByUserAndName(User user, String name) {
        return playlistRepository.findByUserAndName(user, name);
    }

}
