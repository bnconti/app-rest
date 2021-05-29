package ar.edu.unnoba.pdyc.apprest.dto;

import java.util.List;

public class PlaylistWithSongsDTO extends PlaylistDTO {
    private List<SongDTO> songs;

    public List<SongDTO> getSongs() {
        return songs;
    }
    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }
}
