package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistWithSongsDTO;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistService;
import ar.edu.unnoba.pdyc.apprest.service.SongService;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/playlists/{id}")
public class PlaylistResource {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private SongService songsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@PathParam("id") Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            ModelMapper modelMapper = new ModelMapper();
            PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
            return Response.ok(dto).build();
        }
    }

    @DELETE
    public Response deletePlaylist(@PathParam("id") Long id) {
        if (playlistService.deletePlaylist(id)) {
            // playlist eliminada
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
