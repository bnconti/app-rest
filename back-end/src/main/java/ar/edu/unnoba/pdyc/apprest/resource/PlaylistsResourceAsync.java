package ar.edu.unnoba.pdyc.apprest.resource;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import ar.edu.unnoba.pdyc.apprest.dto.PlaylistAddSongDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistUpdateDTO;
import ar.edu.unnoba.pdyc.apprest.dto.PlaylistWithSongsDTO;
import ar.edu.unnoba.pdyc.apprest.model.Playlist;
import ar.edu.unnoba.pdyc.apprest.model.Song;
import ar.edu.unnoba.pdyc.apprest.service.PlaylistService;
import ar.edu.unnoba.pdyc.apprest.service.SongService;

@Path("/playlists")
public class PlaylistsResourceAsync {
    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAll(@Suspended AsyncResponse response) {
        playlistService.getPlaylistsAsync().thenAccept((playlists) -> {
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
        playlistService.getPlaylistByIdAsync(id).thenAccept((playlist) -> {
            if (playlist == null) {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            } else {
                ModelMapper modelMapper = new ModelMapper();
                PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
                response.resume(Response.ok(dto).build());
            }
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(@Suspended AsyncResponse response, PlaylistDTO dto) {
        if (dto == null) {
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        String ownerEmail = Util.getLoggedUser();
        if (ownerEmail == null) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        ModelMapper modelMapper = new ModelMapper();
        Playlist playlist = modelMapper.map(dto, Playlist.class);
        playlistService.createAsync(playlist, ownerEmail).handle((p, exception) -> {
            if (exception == null) {
                return response.resume(Response.ok().build());
            } else if (exception.getCause() instanceof DataIntegrityViolationException) {
                // se quiso crear una lista ya existente
                return response.resume(Response.status(Response.Status.CONFLICT).build());
            } else {
                exception.printStackTrace();
                return response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            }
        });
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void rename(@Suspended AsyncResponse response, @PathParam("id") Long id, PlaylistUpdateDTO dto) {
        if (dto == null) {
            response.resume(Response.status(Response.Status.BAD_REQUEST).build());
            return;
        }

        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        playlistService.getPlaylistByIdAsync(id).thenAccept((playlist) -> {
            if (playlist == null) {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
                return;
            }

            if (!playlist.getUser().getEmail().equals(authUserEmail)) {
                response.resume(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

            playlist.setName(dto.getName());
            playlistService.updateAsync(playlist).handle((p, exception) -> {
                if (exception == null) {
                    return response.resume(Response.ok().build());
                } else if (exception.getCause() instanceof DataIntegrityViolationException) {
                    // se quiso renombrar a un nombre ya existente
                    return response.resume(Response.status(Response.Status.CONFLICT).build());
                } else {
                    System.out.println(exception);
                    return response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                }
            });
        });
    }


    @PUT
    @Path("/{id}/songs")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addSong(@Suspended AsyncResponse response, @PathParam("id") Long id, PlaylistAddSongDTO dto) {
        // buscar simultáneamente la playlist y la canción
        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        playlistService.getPlaylistByIdAsync(id).thenCombine(
                songService.getSongByIdAsync(dto.getSongId()), (playlist, song) -> {

            if (playlist == null || song == null) {
                // no se encontró la playlist o la canción
                return response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }

            String ownerEmail = playlist.getUser().getEmail();
            if (!ownerEmail.equals(authUserEmail)) {
                // no coinciden los emails
                return response.resume(Response.status(Response.Status.FORBIDDEN).build());
            }

            List<Song> songs = playlist.getSongs();
            if (songs.contains(song)) {
                // la canción ya está en la lista
                return response.resume(Response.status(Response.Status.CONFLICT).build());
            }

            songs.add(song);
            return playlistService.updateAsync(playlist).handle((p, exception) -> {
                if (exception == null) {
                    return response.resume(Response.ok().build());
                } else {
                    exception.printStackTrace();
                    return response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                }
            });
        });
    }

    @DELETE
    @Path("/{id}/songs/{song_id}")
    public void removeSong(@Suspended AsyncResponse response, @PathParam("id") Long id,
            @PathParam("song_id") Long songId) {
        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        // buscar simultáneamente la playlist y la canción
        playlistService.getPlaylistByIdAsync(id).thenCombine(
                songService.getSongByIdAsync(songId), (playlist, song) -> {

            if (playlist == null || song == null) {
                // no se encontró la playlist o la canción
                return response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }

            String ownerEmail = playlist.getUser().getEmail();
            if (!ownerEmail.equals(authUserEmail)) {
                return response.resume(Response.status(Response.Status.FORBIDDEN).build());
            }

            List<Song> songs = playlist.getSongs();
            if (!songs.contains(song)) {
                return response.resume(Response.status(Response.Status.NOT_FOUND).build());
            }

            songs.remove(song);
            return playlistService.updateAsync(playlist).handle((p, exception) -> {
                if (exception == null) {
                    return response.resume(Response.ok().build());
                } else {
                    exception.printStackTrace();
                    return response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                }
            });
        });
    }

    @DELETE
    @Path("/{id}")
    public void delete(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        String authUserEmail = Util.getLoggedUser();
        if (authUserEmail == null) {
            response.resume(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }

        playlistService.getPlaylistByIdAsync(id).thenAccept((playlist) -> {
            if (playlist == null) {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
                return;
            }

            String ownerEmail = playlist.getUser().getEmail();
            if (!ownerEmail.equals(authUserEmail)) {
                response.resume(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

            playlistService.deleteAsync(playlist.getId()).handle((deleted, exception) -> {
                if (exception == null && deleted) {
                    return response.resume(Response.ok().build());
                } else {
                    exception.printStackTrace();
                    return response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                }
            });
        });
    }
}
