package ar.edu.unnoba.pdyc.apprest.resource;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.unnoba.pdyc.apprest.dto.SongDTO;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.service.SongService;

@Path("/songs")
public class SongsResourceSync {
    @Autowired
    private SongService songsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSongs(@QueryParam("author") String author, @QueryParam("genre") String genre) {
        List<Song> songs = songsService.getSongsByAuthorAndGenre(author, genre);

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SongDTO>>() {
        }.getType();
        List<SongDTO> dtos = modelMapper.map(songs, listType);
        return Response.ok(dtos).build();
    }
}
