package ar.edu.unnoba.pdyc.apprest.dto;

import java.util.List;

public class PlaylistWithSongsDTO extends PlaylistDTO {
    private static final long serialVersionUID = 1L;

    private List<SongDTO> songs;

    @Override
    public List<SongDTO> getSongs() {
        return songs;
    }
    @Override
    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }
}
