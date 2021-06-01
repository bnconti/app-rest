package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.apprest.repository.SongRepository;
import ar.edu.unnoba.pdyc.apprest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImp implements PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean exists(Long id) {
        return playlistRepository.existsById(id);
    }

    @Override
    public void create(Playlist newPlaylist, String ownerEmail) {
        newPlaylist.setUser(userRepository.findByEmail(ownerEmail));
        playlistRepository.save(newPlaylist);
    }

    @Override
    public void update(Playlist updatedPlaylist) {
        playlistRepository.save(updatedPlaylist);
    }

    @Override
    public Boolean delete(Long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        if (playlist.isEmpty()) {
            return false;
        }
        playlistRepository.delete(playlist.get());
        return true;
    }

    @Override
    public List<Playlist> getPlaylists() {
        return playlistRepository.findAll();
    }

    @Override
    public Playlist getPlaylistById(Long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        return (playlist.isEmpty() ? null : playlist.get());
    }

    @Override
    public List<Playlist> getPlaylistsByUser(User user) {
        return playlistRepository.findByUser(user);
    }

    @Override
    public Playlist getPlaylistByUserAndName(User user, String name) {
        return playlistRepository.findByUserAndName(user, name);
    }
}
