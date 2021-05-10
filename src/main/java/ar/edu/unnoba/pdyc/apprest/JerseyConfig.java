package ar.edu.unnoba.pdyc.apprest;

import ar.edu.unnoba.pdyc.apprest.resource.SongResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // TODO: Agregar los otros resources
        register(SongResource.class);
    }
}
