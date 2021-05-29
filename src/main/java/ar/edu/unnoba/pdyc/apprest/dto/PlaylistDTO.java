package ar.edu.unnoba.pdyc.apprest.dto;

import java.io.Serializable;
import java.util.List;

public class PlaylistDTO implements Serializable {
    private Long id;
    private String name;
    private UserDTO user;
    private List<SongDTO> songs;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }
}
