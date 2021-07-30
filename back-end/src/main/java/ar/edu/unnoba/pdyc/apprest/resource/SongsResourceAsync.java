package ar.edu.unnoba.pdyc.apprest.resource;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ar.edu.unnoba.pdyc.apprest.model.Song;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unnoba.pdyc.apprest.dto.SongDTO;
import ar.edu.unnoba.pdyc.apprest.service.SongService;

@Path("/songs")
public class SongsResourceAsync {
    @Autowired
    private SongService songsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getSongs(@Suspended AsyncResponse response,
            @QueryParam("author") String author, @QueryParam("genre") String genre) {
        songsService.getSongsByAuthorAndGenreAsync(author, genre).thenAccept((songs) -> {
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<SongDTO>>() {
            }.getType();
            List<SongDTO> dtos = modelMapper.map(songs, listType);
            response.resume(Response.ok(dtos).build());
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void create(@Suspended AsyncResponse response, SongDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Song song = modelMapper.map(dto, Song.class);

        songsService.createAsync(song).thenAccept((newSong) ->
                response.resume(Response.ok(modelMapper.map(newSong, SongDTO.class)).build()));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void update(@Suspended AsyncResponse response, SongDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Song song = modelMapper.map(dto, Song.class);

        songsService.updateAsync(song).thenAccept((updatedSong) ->
                response.resume(Response.ok(modelMapper.map(updatedSong, SongDTO.class)).build()));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        songsService.deleteAsync(id).thenAccept((status) ->
                response.resume(Response.ok(status).build()));
    }
}
