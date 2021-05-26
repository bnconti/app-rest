package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/playlists")
public class PlaylistResource {
    @Autowired
    private PlaylistService playlistService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists() {
        List<Playlist> playlists = playlistService.getPlaylists();
        List<Playlist> dtos = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (Playlist playlist : playlists) {
            dtos.add(modelMapper.map(playlist, Playlist.class));
        }
        return Response.ok(dtos).build();
    }
}
