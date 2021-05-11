package ar.edu.unnoba.pdyc.apprest.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "songs", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "author"}))
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String author;

    @Enumerated(EnumType.ORDINAL)
    private Genre genre;


    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
