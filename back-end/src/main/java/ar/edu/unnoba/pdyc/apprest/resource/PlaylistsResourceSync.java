package ar.edu.unnoba.pdyc.apprest.resource;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import ar.edu.unnoba.pdyc.apprest.dto.*;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistsService;
import ar.edu.unnoba.pdyc.apprest.service.SongsService;

/* Recurso playlists - versión sincrónica */
@Path("/playlists")
public class PlaylistsResourceSync {
    @Autowired
    private PlaylistsService playlistsService;

    @Autowired
    private SongsService songsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Playlist> playlists = playlistsService.getPlaylists();

        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<PlaylistDTO>>() {
        }.getType();
        List<PlaylistDTO> dtos = modelMapper.map(playlists, listType);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        Playlist playlist = playlistsService.getPlaylistById(id);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ModelMapper modelMapper = new ModelMapper();
        PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PlaylistDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String ownerEmail = Util.getLoggedUser();
        if (ownerEmail == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        ModelMapper modelMapper = new ModelMapper();
        Playlist playlist = modelMapper.map(dto, Playlist.class);
        try {
            playlistsService.create(playlist, ownerEmail);
            return Response.ok().build();
        } catch (DataIntegrityViolationException exception) {
            // probablemente se quiso crear una lista que ya existía
            return Response.status(Response.Status.CONFLICT).build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rename(@PathParam("id") Long id, PlaylistUpdateDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Playlist playlist = playlistsService.getPlaylistById(id);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!authUserEmail.equals(playlist.getUser().getEmail())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        playlist.setName(dto.getName());
        try {
            playlistsService.update(playlist);
            return Response.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSong(@PathParam("id") Long id, PlaylistAddSongDTO dto) {
        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Playlist playlist = playlistsService.getPlaylistById(id);
        if (playlist == null) {
            // no se encontró la playlist
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String ownerEmail = playlist.getUser().getEmail();
        if (!ownerEmail.equals(authUserEmail)) {
            // no coinciden los emails
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Song song = songsService.getSongById(dto.getSongId());
        if (song == null) {
            // no se encontró la canción
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Song> songs = playlist.getSongs();
        if (songs.contains(song)) {
            // la canción ya está en la lista
            return Response.status(Response.Status.CONFLICT).build();
        }

        songs.add(song);
        try {
            playlistsService.update(playlist);
            return Response.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}/songs/{song_id}")
    public Response removeSong(@PathParam("id") Long id, @PathParam("song_id") Long songId) {
        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Playlist playlist = playlistsService.getPlaylistById(id);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String ownerEmail = playlist.getUser().getEmail();
        if (!ownerEmail.equals(authUserEmail)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Song song = songsService.getSongById(songId);
        if (song == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Song> songs = playlist.getSongs();
        if (!songs.contains(song)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        songs.remove(song);
        try {
            playlistsService.update(playlist);
            return Response.ok().build();
        } catch (Exception exception) {
            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Playlist playlist = playlistsService.getPlaylistById(id);
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String ownerEmail = playlist.getUser().getEmail();
        if (!ownerEmail.equals(authUserEmail)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (!playlistsService.delete(id)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().build();
    }
}
