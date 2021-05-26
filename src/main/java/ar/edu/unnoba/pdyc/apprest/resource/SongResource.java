package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.SongDto;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.service.SongService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/songs")
public class SongResource {
    @Autowired
    private SongService songService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSongs() {
        List<Song> songs = songService.getSongs();
        List<SongDto> dtos = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        for (Song song : songs) {
            dtos.add(modelMapper.map(song, SongDto.class));
        }
        return Response.ok(dtos).build();
    }
}
