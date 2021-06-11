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

// FIXME: Según el depurador, con cada request se crea un thread que se queda ejecutando indeterminadamente
// FIXME: No se obtiene respuesta si ocurre una excepción de SQL por ejemplo
@Path("/playlists")
public class PlaylistsResource {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    private Response getPlaylistsResponse(List<Playlist> playlists) {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<PlaylistDTO>>() {
        }.getType();
        List<PlaylistDTO> dtos = modelMapper.map(playlists, listType);
        return Response.ok(dtos).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylists(@Suspended AsyncResponse response) {
        playlistService.getPlaylistsAsync().thenAccept(
                (playlists) -> response.resume(getPlaylistsResponse(playlists)));
    }

    private Response getPlaylistsResponse(Playlist playlist) {
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ModelMapper modelMapper = new ModelMapper();
        PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getPlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        playlistService.getPlaylistByIdAsync(id).thenAccept(
                (playlist) -> response.resume(getPlaylistsResponse(playlist)));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createPlaylist(@Suspended AsyncResponse response, PlaylistDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        if (dto == null) {
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        String ownerEmail = authentication.getPrincipal().toString();
        ModelMapper modelMapper = new ModelMapper();
        Playlist p = modelMapper.map(dto, Playlist.class);
        playlistService.createAsync(p, ownerEmail).thenAccept(
                (playlist) -> response.resume(Response.ok().build()));
    }

    private Response getUpdatePlaylistNameResponse(Playlist playlist, String newName) {
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String authUserEmail = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString();

        if (!authUserEmail.equals(playlist.getUser().getEmail())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        try {
            playlist.setName(newName);
            // FIXME: se pueden manejar mejor estos casos?
            playlistService.updateAsync(playlist).get();
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePlaylistName(@Suspended AsyncResponse response, @PathParam("id") Long id, PlaylistUpdateDTO dto) {
        if (dto == null) {
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        playlistService.getPlaylistByIdAsync(id).thenAccept(
                (playlist) -> response.resume(getUpdatePlaylistNameResponse(playlist, dto.getName())));
    }

    private Response getAddSongToPlaylistResponse(Playlist playlist, Long songId) {
        if (playlist == null) {
            // no se encontró la playlist
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String ownerEmail = playlist.getUser().getEmail();

        if (!authUserEmail.equals(ownerEmail)) {
            // no coinciden los emails
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Song song;
        try {
            song = songService.getSongByIdAsync(songId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (song == null) {
            // no se encontró la canción
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        for (Song s : playlist.getSongs()) {
            if (s.getId().equals(songId)) {
                // la canción ya está en la lista
                return Response.status(Response.Status.CONFLICT).build();
            }
        }

        playlist.getSongs().add(song);
        try {
            playlistService.updateAsync(playlist).get();
            return Response.ok().build();
        } catch (Exception e) {
            // otro tipo de error inesperado
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSongToPlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id, PlaylistAddSongDTO dto) {
        playlistService.getPlaylistByIdAsync(id).thenAccept(
                (playlist) -> response.resume(getAddSongToPlaylistResponse(playlist, dto.getSongId())));
    }

    public Response getRemoveSongFromPlaylistResponse(Playlist playlist, Long songId) {
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String ownerEmail = playlist.getUser().getEmail();

        if (!ownerEmail.equals(authUserEmail)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // FIXME
        Song songToRemove;
        try {
            songToRemove = songService.getSongByIdAsync(songId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (songToRemove == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!playlist.getSongs().contains(songToRemove)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            playlist.getSongs().remove(songToRemove);
            playlistService.updateAsync(playlist).get();
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/songs/{song_id}")
    public void removeSongFromPlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id,
            @PathParam("song_id") Long songId) {

        playlistService.getPlaylistByIdAsync(id).thenAccept(
                (playlist) -> response.resume(getRemoveSongFromPlaylistResponse(playlist, songId)));
    }

    private Response getDeletePlaylistResponse(Playlist playlist) {
        if (playlist == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String ownerUserEmail = playlist.getUser().getEmail();

        if (!authUserEmail.equals(ownerUserEmail)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Boolean deleted;
        try {
            deleted = playlistService.deleteAsync(playlist.getId()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (!deleted) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public void deletePlaylist(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        playlistService.getPlaylistByIdAsync(id).thenAccept(
                (playlist) -> response.resume(getDeletePlaylistResponse(playlist)));
    }
}
