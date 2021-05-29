package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

@Path("/playlists")
public class PlaylistsResource {
    @Autowired
    private PlaylistService playlistsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists() {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<PlaylistDTO>>(){}.getType();
        List<Playlist> playlists = playlistsService.getPlaylists();
        List<PlaylistDTO> dtos = modelMapper.map(playlists, listType);
        return Response.ok(dtos).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPlaylist(PlaylistDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ModelMapper modelMapper = new ModelMapper();
        Playlist p = modelMapper.map(dto, Playlist.class);
        // persistir
        playlistsService.putPlaylist(p);

        // el id puede cambiar, retornar el real
        User user = modelMapper.map(dto.getUser(), User.class);
        playlistsService.getPlaylistByUserAndName(user, dto.getName());

        String strUri = "/playlists/" + dto.getId().toString();
        return Response.created(URI.create(strUri)).build();
    }
}
