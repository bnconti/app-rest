package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists/{id}")
public class PlaylistResource {
    @Autowired
    private PlaylistService playlistService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@PathParam("id") Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (playlist == null) {
            // retornar 404
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            ModelMapper modelMapper = new ModelMapper();
            PlaylistDTO dto = modelMapper.map(playlist, PlaylistDTO.class);
            return Response.ok(dto).build();
        }
    }
}
