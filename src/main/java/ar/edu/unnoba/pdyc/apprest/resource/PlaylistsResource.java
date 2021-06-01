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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Path("/playlists")
public class PlaylistsResource {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists() {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<PlaylistDTO>>() {
        }.getType();
        List<Playlist> playlists = playlistService.getPlaylists();
        List<PlaylistDTO> dtos = modelMapper.map(playlists, listType);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaylists(@PathParam("id") Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (playlist != null) {
            ModelMapper modelMapper = new ModelMapper();
            PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
            return Response.ok(dto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPlaylist(PlaylistDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String ownerEmail = authentication.getPrincipal().toString();
            ModelMapper modelMapper = new ModelMapper();
            Playlist p = modelMapper.map(dto, Playlist.class);
            playlistService.create(p, ownerEmail);
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePlaylistName(@PathParam("id") Long id, PlaylistUpdateDTO dto) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Playlist oldPlaylist = playlistService.getPlaylistById(id);

        if (oldPlaylist != null) {
            String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            if (authUserEmail.equals(oldPlaylist.getUser().getEmail())) {
                try {
                    oldPlaylist.setName(dto.getName());
                    playlistService.update(oldPlaylist);
                    return Response.ok().build();
                } catch (Exception e) {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSongToPlaylist(@PathParam("id") Long id, PlaylistAddSongDTO dto) {

        Playlist playlist = playlistService.getPlaylistById(id);

        if (playlist != null) {

            String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            String ownerEmail = playlist.getUser().getEmail();

            if (authUserEmail.equals(ownerEmail)) {
                Song song = songService.getSongById(dto.getSongId());
                if (song != null) {
                    for (Song s : playlist.getSongs()) {
                        if (s.getId().equals(dto.getSongId())) {
                            // la canción ya está en la lista
                            return Response.status(Response.Status.CONFLICT).build();
                        }
                    }
                    playlist.getSongs().add(song);
                    try {
                        playlistService.update(playlist);
                        return Response.ok().build();
                    } catch (Exception e) {
                        // otro tipo de error inesperado
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                    }
                } else {
                    // no se encontró la canción
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            } else {
                // no coinciden los emails
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        // no se encontró la playlist
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/songs/{song_id}")
    public Response removeSongFromPlaylist(@PathParam("id") Long id, @PathParam("song_id") Long songId) {
        String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Playlist playlist = playlistService.getPlaylistById(id);

        // TODO: cómo hacer esto sin que sea un laberinto?
        if (playlist != null) {
            String ownerEmail = playlist.getUser().getEmail();
            if (ownerEmail.equals(authUserEmail)) {
                Song songToRemove = songService.getSongById(songId);
                if (songToRemove != null) {
                    if (playlist.getSongs().contains(songToRemove)) {
                        try {
                            playlist.getSongs().remove(songToRemove);
                            playlistService.update(playlist);
                            return Response.ok().build();
                        } catch (Exception e) {
                            // otro tipo de error inesperado
                            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                        }
                    } else {
                        // la canción no está en la playlist
                        return Response.status(Response.Status.NOT_FOUND).build();
                    }
                }
                // la canción no existe
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            // no coinciden los emails
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        // no se encontró la playlist
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePlaylist(@PathParam("id") Long id) {
        Playlist playlist = playlistService.getPlaylistById(id);

        if (playlist != null) {
            String authUserEmail = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

            String ownerUserEmail = playlist.getUser().getEmail();

            if (authUserEmail.equals(ownerUserEmail)) {
                if (playlistService.delete(id)) {
                    return Response.ok().build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                // el usuario no es el creador de la playlist
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        }
        // no se encontró la playlist
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
