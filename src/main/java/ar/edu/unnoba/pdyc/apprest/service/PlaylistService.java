package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlaylistService {
    /* variantes sincrónicas */

    Boolean exists(Long id);

    List<Playlist> getPlaylists();
    List<Playlist> getPlaylistsByUser(User user);

    Playlist getPlaylistById(Long id);
    Playlist getPlaylistByUserAndName(User user, String name);

    Playlist create(Playlist playlist, String userEmail);
    Playlist update(Playlist updatedPlaylist);
    Boolean delete(Long id);

   
    /* variantes asincrónicas */

    CompletableFuture<Boolean> existsAsync(Long id);

    CompletableFuture<List<Playlist>> getPlaylistsAsync();
    CompletableFuture<List<Playlist>> getPlaylistsByUserAsync(User user);

    CompletableFuture<Playlist> getPlaylistByIdAsync(Long id);
    CompletableFuture<Playlist> getPlaylistByUserAndNameAsync(User user, String name);

    CompletableFuture<Playlist> createAsync(Playlist playlist, String userEmail);
    CompletableFuture<Playlist> updateAsync(Playlist updatedPlaylist);
    CompletableFuture<Boolean> deleteAsync(Long id);
}
