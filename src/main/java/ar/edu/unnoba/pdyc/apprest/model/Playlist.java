package ar.edu.unnoba.pdyc.apprest.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = User.class, optional = false)
    private User user;

    @ManyToMany(targetEntity = Song.class)
    @JoinTable(
            name = "playlists_songs",
            joinColumns = @JoinColumn(name="playlist_id", referencedColumnName="id", nullable = false),
            inverseJoinColumns=@JoinColumn(name="song_id", referencedColumnName="id", nullable = false)
    )
    private List<Song> songs = new ArrayList<>();


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Song> getSongs() { return songs; }
    public void setSongs(List<Song> songs) { this.songs = songs; }
}
