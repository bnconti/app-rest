package ar.edu.unnoba.pdyc.apprest.dto;

import ar.edu.unnoba.pdyc.apprest.model.Genre;

import java.io.Serializable;

public class SongDto implements Serializable {
    private String name;
    private String author;
    private Genre genre;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) { this.genre = genre; }
}
