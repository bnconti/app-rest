package ar.edu.unnoba.pdyc.apprest.dto;

import ar.edu.unnoba.pdyc.apprest.model.User;

import java.io.Serializable;

public class PlaylistDto implements Serializable {
    private String name;
    private User user;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
