package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlaylistService {
    Boolean exists(Long id);
    
    CompletableFuture<List<Playlist>> getPlaylists();
    CompletableFuture<List<Playlist>> getPlaylistsByUser(User user);

    CompletableFuture<Playlist> getPlaylistById(Long id);
    CompletableFuture<Playlist> getPlaylistByUserAndName(User user, String name);

    CompletableFuture<Playlist> create(Playlist playlist, String userEmail);
    CompletableFuture<Playlist> update(Playlist updatedPlaylist);
    CompletableFuture<Boolean> delete(Long id);
}
