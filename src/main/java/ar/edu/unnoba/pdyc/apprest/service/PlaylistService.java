package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;

import java.util.List;

public interface PlaylistService {
    Boolean exists(Long id);
    
    List<Playlist> getPlaylists();
    List<Playlist> getPlaylistsByUser(User user);

    Playlist getPlaylistById(Long id);
    Playlist getPlaylistByUserAndName(User user, String name);

    void create(Playlist playlist, String userEmail);
    void update(Playlist updatedPlaylist);
    Boolean delete(Long id);
}
