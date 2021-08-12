package ar.edu.unnoba.pdyc.apprest.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.PlaylistsRepository;
import ar.edu.unnoba.pdyc.apprest.repository.UsersRepository;

@Service
public class PlaylistsServiceImp implements PlaylistsService {
    @Autowired
    private PlaylistsRepository playlistsRepository;
    @Autowired
    private UsersRepository usersRepository;

    /*** variantes sincrónicas ***/

    @Override
    public Boolean exists(Long id) {
        return playlistsRepository.existsById(id);
    }

    @Override
    public List<Playlist> getPlaylists() {
        return playlistsRepository.findAll();
    }

    @Override
    public Playlist getPlaylistById(Long id) {
        Optional<Playlist> optplaylist = playlistsRepository.findById(id);
        return (optplaylist.isEmpty() ? null : optplaylist.get());
    }

    @Override
    public List<Playlist> getPlaylistsByUser(User user) {
        return playlistsRepository.findByUser(user);
    }

    @Override
    public Playlist getPlaylistByUserAndName(User user, String name) {
        return playlistsRepository.findByUserAndName(user, name);
    }

    @Override
    public Playlist create(Playlist newPlaylist, String ownerEmail) {
        newPlaylist.setUser(usersRepository.findByEmail(ownerEmail));
        return playlistsRepository.save(newPlaylist);
    }

    @Override
    public Playlist update(Playlist updatedPlaylist) {
        return playlistsRepository.save(updatedPlaylist);
    }

    @Override
    public Boolean delete(Long id) {
        Optional<Playlist> optplaylist = playlistsRepository.findById(id);
        if (optplaylist.isEmpty()) {
            return false;
        }
        playlistsRepository.delete(optplaylist.get());
        return true;
    }


    /*** variantes asincrónicas - llaman a las funciones sincrónicas definidas arriba ***/

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> existsAsync(Long id) {
        return CompletableFuture.completedFuture(exists(id));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylistsAsync() {
        return CompletableFuture.completedFuture(getPlaylists());
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> getPlaylistByIdAsync(Long id) {
        return CompletableFuture.completedFuture(getPlaylistById(id));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Playlist>> getPlaylistsByUserAsync(User user) {
        return CompletableFuture.completedFuture(getPlaylistsByUser(user));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> getPlaylistByUserAndNameAsync(User user, String name) {
        return CompletableFuture.completedFuture(getPlaylistByUserAndName(user, name));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> getPlaylistByUserAndNameAsync(String userEmail, String name) {
        User user = usersRepository.findByEmail(userEmail);
        return getPlaylistByUserAndNameAsync(user, name);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> createAsync(Playlist newPlaylist, String ownerEmail) {
        return CompletableFuture.completedFuture(create(newPlaylist, ownerEmail));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Playlist> updateAsync(Playlist updatedPlaylist) {
        return CompletableFuture.completedFuture(update(updatedPlaylist));
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Boolean> deleteAsync(Long id) {
        return CompletableFuture.completedFuture(delete(id));
    }
}
