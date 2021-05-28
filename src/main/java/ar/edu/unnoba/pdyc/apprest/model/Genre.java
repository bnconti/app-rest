package ar.edu.unnoba.pdyc.apprest.model;

public enum Genre {
    ROCK("Rock"),
    TECHNO("Techno"),
    POP("Pop"),
    JAZZ("Jazz"),
    FOLK("Folk"),
    CLASSICAL("Classical"),
    REGGAE("Reggae"),
    CUMBIA("Cumbia");

    private final String descripcion;

    Genre(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
