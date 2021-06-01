package ar.edu.unnoba.pdyc.apprest;

import ar.edu.unnoba.pdyc.apprest.resource.PlaylistsResource;
import ar.edu.unnoba.pdyc.apprest.resource.SongsResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath(AppRestApplication.APP_PATH)
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(SongsResource.class);
        register(PlaylistsResource.class);
    }
}
