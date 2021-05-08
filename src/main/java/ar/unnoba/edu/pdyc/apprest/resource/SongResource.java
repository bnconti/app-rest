package ar.unnoba.edu.pdyc.apprest.resource;

import ar.unnoba.edu.pdyc.apprest.model.Song;
import ar.unnoba.edu.pdyc.apprest.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/songs")
public class SongResource {
    @Autowired
    private SongService songService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSongs() {
        // TODO: Aplicar DTO
        List<Song> songs = songService.getSongs();
        return Response.ok(songs).build();
    }
}
