package ar.edu.unnoba.pdyc.apprest.resource;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unnoba.pdyc.apprest.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistsService;
import ar.edu.unnoba.pdyc.apprest.service.UsersService;

/**
 * Similar a /playlists, pero solo retorna las listas del usuario logueado actualmente.
 */
@Path("/userplaylists")
public class UserPlaylistsResourceAsync {
    @Autowired
    private PlaylistsService playlistsService;

    @Autowired
    private UsersService usersService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAll(@Suspended AsyncResponse response) {
	String loggedEmail = Util.getLoggedUser();
        if (loggedEmail == null) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        User u = usersService.getByEmail(loggedEmail);
        if (u == null) {
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        playlistsService.getPlaylistsByUserAsync(u).thenAccept(playlists -> {
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<PlaylistDTO>>() {
            }.getType();
            List<PlaylistDTO> dtos = modelMapper.map(playlists, listType);
            response.resume(Response.ok(dtos).build());
        });
    }
}
