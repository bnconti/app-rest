package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.PlaylistRepository;
import ar.edu.unnoba.pdyc.apprest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylists() {
        List<Playlist> playlists = playlistRepository.findAll();
        return CompletableFuture.completedFuture(playlists);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> getPlaylistById(Long id) {
        Optional<Playlist> optplaylist = playlistRepository.findById(id);
        Playlist playlist = (optplaylist.isEmpty() ? null : optplaylist.get());
        return CompletableFuture.completedFuture(playlist);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylistsByUser(User user) {
        List<Playlist> playlists = playlistRepository.findByUser(user);
        return CompletableFuture.completedFuture(playlists);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> getPlaylistByUserAndName(User user, String name) {
        Playlist playlist = playlistRepository.findByUserAndName(user, name);
        return CompletableFuture.completedFuture(playlist);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> create(Playlist newPlaylist, String ownerEmail) {
        newPlaylist.setUser(userRepository.findByEmail(ownerEmail));
        Playlist playlist = playlistRepository.save(newPlaylist);
        return CompletableFuture.completedFuture(playlist);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> update(Playlist updatedPlaylist) {
        Playlist playlist = playlistRepository.save(updatedPlaylist);
        return CompletableFuture.completedFuture(playlist);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> delete(Long id) {
        Optional<Playlist> playlist = playlistRepository.findById(id);
        boolean deleted;
        if (playlist.isEmpty()) {
            deleted = false;
        } else {
            playlistRepository.delete(playlist.get());
            deleted = true;
        }
        return CompletableFuture.completedFuture(deleted);
    }
}
