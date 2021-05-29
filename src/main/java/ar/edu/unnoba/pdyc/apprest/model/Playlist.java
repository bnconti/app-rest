package ar.edu.unnoba.pdyc.apprest.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "user_id"}))
public class Playlist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = User.class, optional = false)
    private User user;

    // Pongo EAGER para que funcione la lista de canciones en el recurso playlist
    @ManyToMany(targetEntity = Song.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "playlists_songs",
            joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id", nullable = false)
    )
    private List<Song> songs = new ArrayList<>();


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

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public List<Song> getSongs() {
        return songs;
    }
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
