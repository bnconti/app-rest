package ar.edu.unnoba.pdyc.apprest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import ar.edu.unnoba.pdyc.apprest.resource.PlaylistsResourceAsync;
import ar.edu.unnoba.pdyc.apprest.resource.SongsResourceAsync;
import ar.edu.unnoba.pdyc.apprest.resource.UserResourceSync;
import ar.edu.unnoba.pdyc.apprest.resource.UserPlaylistsResourceAsync;

@Component
@ApplicationPath(AppRestApplication.APP_PATH)
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        //register(SongsResourceSync.class);
        register(SongsResourceAsync.class);
        //register(PlaylistsResourceSync.class);
        register(PlaylistsResourceAsync.class);
        register(UserPlaylistsResourceAsync.class);
        register(SongsResourceAsync.class);
        register(UserResourceSync.class);
    }
}
