package ar.edu.unnoba.pdyc.apprest;

import ar.edu.unnoba.pdyc.apprest.resource.PlaylistResource;
import ar.edu.unnoba.pdyc.apprest.resource.SongResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/music")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(PlaylistResource.class);
        register(SongResource.class);
    }
}
