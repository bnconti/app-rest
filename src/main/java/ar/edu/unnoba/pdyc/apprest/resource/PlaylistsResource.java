package ar.edu.unnoba.pdyc.apprest.resource;

import ar.edu.unnoba.pdyc.apprest.dto.PlaylistAddSongDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistUpdateDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistWithSongsDTO;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistService;
import ar.edu.unnoba.pdyc.apprest.service.SongService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

// TODO: Revisar si está bien, y qué tendría que ir dentro de las funciones llamadas por thenAccept
@Path("/playlists")
public class PlaylistsResource {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylists(@Suspended AsyncResponse response) {
        playlistService.getPlaylists().thenAccept((playlists) -> {
            ModelMapper modelMapper = new ModelMapper();
            Type listType = new TypeToken<List<PlaylistDTO>>() {
            }.getType();
            List<PlaylistDTO> dtos = modelMapper.map(playlists, listType);
            response.resume(Response.ok(dtos).build());
        });
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        playlistService.getPlaylistById(id).thenAccept((playlist) -> {
            if (playlist != null) {
                ModelMapper modelMapper = new ModelMapper();
                PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
                response.resume(Response.ok(dto).build());
            } else {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createPlaylist(@Suspended AsyncResponse response, PlaylistDTO dto) {
        if (dto == null) {
            // TODO: está bien poner resume acá?
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ((authentication instanceof AnonymousAuthenticationToken)) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        String ownerEmail = authentication.getPrincipal().toString();
        ModelMapper modelMapper = new ModelMapper();
        Playlist p = modelMapper.map(dto, Playlist.class);
        playlistService.create(p, ownerEmail).thenAccept((playlist) -> {
            response.resume(Response.ok().build());
        });
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlaylistName(@Suspended AsyncResponse response, @PathParam("id") Long id, PlaylistUpdateDTO dto) {
        if (dto == null) {
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        Playlist oldPlaylist;
        try {
            // TODO: Revisar. Seguro que está mal lo del get() y tendría que estar todo
            // dentro de thenAccept
            oldPlaylist = playlistService.getPlaylistById(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }

        if (oldPlaylist == null) {
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }
        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (!authUserEmail.equals(oldPlaylist.getUser().getEmail())) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        try {
            playlistService.update(oldPlaylist).thenAccept((playlist) -> {
                oldPlaylist.setName(dto.getName());
                response.resume(Response.ok().build());
            });
        } catch (Exception e) {
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        }
    }

    @PUT
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSongToPlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id, PlaylistAddSongDTO dto) {

        Playlist playlist;
        // TODO: Revisar
        try {
            playlist = playlistService.getPlaylistById(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }

        if (playlist == null) {
            // no se encontró la playlist
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String ownerEmail = playlist.getUser().getEmail();

        if (!authUserEmail.equals(ownerEmail)) {
            // no coinciden los emails
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        Song song;
        try {
            song = songService.getSongById(dto.getSongId()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }
        if (song == null) {
            // no se encontró la canción
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        for (Song s : playlist.getSongs()) {
            if (s.getId().equals(dto.getSongId())) {
                // la canción ya está en la lista
                response.resume(Response.status(Response.Status.CONFLICT).build());
                return;
            }
        }

        playlist.getSongs().add(song);
        try {
            playlistService.update(playlist);
            response.resume(Response.ok().build());
        } catch (Exception e) {
            // otro tipo de error inesperado
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        }
    }

    @PUT
    @Path("/{id}/songs/{song_id}")
    public void removeSongFromPlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id,
            @PathParam("song_id") Long songId) {
        Playlist playlist;
        try {
            playlist = playlistService.getPlaylistById(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }

        if (playlist == null) {
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String ownerEmail = playlist.getUser().getEmail();

        if (!ownerEmail.equals(authUserEmail)) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        Song songToRemove;
        try {
            songToRemove = songService.getSongById(songId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }
        if (songToRemove == null) {
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        if (!playlist.getSongs().contains(songToRemove)) {
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        try {
            playlist.getSongs().remove(songToRemove);
            playlistService.update(playlist);
            response.resume(Response.ok().build());
        } catch (Exception e) {
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        }

    }

    @DELETE
    @Path("/{id}")
    public void deletePlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        Playlist playlist;
        try {
            playlist = playlistService.getPlaylistById(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }

        if (playlist == null) {
            response.resume(Response.status(Response.Status.NOT_FOUND).build());
            return;
        }

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String ownerUserEmail = playlist.getUser().getEmail();

        if (!authUserEmail.equals(ownerUserEmail)) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        boolean deleted = false;
        try {
            deleted = playlistService.delete(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        }

        if (deleted) {
            response.resume(Response.ok().build());
        } else {
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
        }
    }
}
