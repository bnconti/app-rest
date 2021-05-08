package ar.unnoba.edu.pdyc.apprest;

import ar.unnoba.edu.pdyc.apprest.model.Song;
import ar.unnoba.edu.pdyc.apprest.resource.SongResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // TODO: Agregar los otros resources
        register(SongResource.class);
    }
}
