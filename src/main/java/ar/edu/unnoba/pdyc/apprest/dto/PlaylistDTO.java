package ar.edu.unnoba.pdyc.apprest.dto;

import ar.edu.unnoba.pdyc.apprest.model.User;

import java.io.Serializable;

public class PlaylistDTO implements Serializable {
    private Long id;
    private String name;
    private User user;

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

    public String getUser() {
        return user.toString();
    }
    public void setUser(User user) {
        this.user = user;
    }
}
