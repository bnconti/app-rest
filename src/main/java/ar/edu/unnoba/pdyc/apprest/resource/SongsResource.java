package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.SongDTO;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.service.SongService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

@Path("/songs")
public class SongsResource {
    @Autowired
    private SongService songsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSongs(@QueryParam("author") String author, @QueryParam("genre") String genre) {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<SongDTO>>(){}.getType();
        List<Song> songs = songsService.getSongsByAuthorAndGenre(author, genre);
        List<SongDTO> dtos = modelMapper.map(songs, listType);
        return Response.ok(dtos).build();
    }
}
