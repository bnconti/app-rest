package ar.edu.unnoba.pdyc.apprest.resource;

import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
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

@Path("/playlists")
public class PlaylistsResourceAsync {
    @Autowired
    private PlaylistsService playlistsService;

    @Autowired
    private SongsService songsService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAll(@Suspended AsyncResponse response) {
        playlistsService.getPlaylistsAsync().thenAccept(playlists -> {
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
    public void getById(@Suspended AsyncResponse response, @PathParam("id") Long id) {
        playlistsService.getPlaylistByIdAsync(id).thenAccept(playlist -> {
            if (playlist == null) {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
            } else {
                ModelMapper modelMapper = new ModelMapper();
                PlaylistWithSongsDTO dto = modelMapper.map(playlist, PlaylistWithSongsDTO.class);
                response.resume(Response.ok(dto).build());
            }
        });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getid")
    public void getIdByUserAndName(@Suspended AsyncResponse response,
                                   @QueryParam("user") String user,
                                   @QueryParam("name") String name) {
        playlistsService.getPlaylistByUserAndNameAsync(user, name).thenAccept(p -> {
            response.resume(Response.ok(p == null ? null : p.getId()).build());
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
        playlistsService.createAsync(playlist, ownerEmail).handle((p, exception) -> {
            if (exception == null) {
                return response.resume(Response.ok(p.getId()).build());
            } else if (exception.getCause() instanceof DataIntegrityViolationException) {
                // Se quiso crear una lista ya existente
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

        playlistsService.getPlaylistByIdAsync(id).thenAccept(playlist -> {
            if (playlist == null) {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
                return;
            }

            if (!playlist.getUser().getEmail().equals(authUserEmail)) {
                response.resume(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

            playlist.setName(dto.getName());
            playlistsService.updateAsync(playlist).handle((p, exception) -> {
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

        playlistsService.getPlaylistByIdAsync(id).thenCombine(
                songsService.getSongByIdAsync(dto.getSongId()), (playlist, song) -> {

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
            return playlistsService.updateAsync(playlist).handle((p, exception) -> {
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
        playlistsService.getPlaylistByIdAsync(id).thenCombine(
                songsService.getSongByIdAsync(songId), (playlist, song) -> {

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
            return playlistsService.updateAsync(playlist).handle((p, exception) -> {
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

        playlistsService.getPlaylistByIdAsync(id).thenAccept(playlist -> {
            if (playlist == null) {
                response.resume(Response.status(Response.Status.NOT_FOUND).build());
                return;
            }

            String ownerEmail = playlist.getUser().getEmail();
            if (!ownerEmail.equals(authUserEmail)) {
                response.resume(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }

            playlistsService.deleteAsync(playlist.getId()).handle((deleted, exception) -> {
                if (exception == null && deleted) {
                    return response.resume(Response.ok(deleted).build());
                } else {
                    exception.printStackTrace();
                    return response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
                }
            });
        });
    }
}
