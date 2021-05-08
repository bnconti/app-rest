package ar.unnoba.edu.pdyc.apprest;

import org.glassfish.jersey.jaxb.internal.XmlJaxbElementProvider;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(XmlJaxbElementProvider.App.class);
    }
}
