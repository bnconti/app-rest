package ar.unnoba.edu.pdyc.apprest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        // Registrar acá las clases

        // register(UserResource.class);
        // ...
    }
}
