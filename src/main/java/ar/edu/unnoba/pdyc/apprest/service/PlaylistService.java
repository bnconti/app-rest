package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;

import java.util.List;

public interface PlaylistService {
    List<Playlist> getPlaylists();
    List<Playlist> getPlaylistsByUser(User user);

    Playlist getPlaylistById(Long id);
    Playlist getPlaylistByUserAndName(User user, String name);
}
