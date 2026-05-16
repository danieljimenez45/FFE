package com.entradas_cine.ffe.cine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PeliculaPosterResourceConfig implements WebMvcConfigurer {

    @Value("${app.peliculas.poster-dir:uploads/peliculas}")
    private String posterDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path dir = Paths.get(posterDir).toAbsolutePath().normalize();
        String fileLocation = dir.toUri().toString();
        if (!fileLocation.endsWith("/")) {
            fileLocation += "/";
        }

        registry.addResourceHandler("/images/peliculas/**")
                .addResourceLocations(
                        "classpath:/static/images/peliculas/",
                        fileLocation
                );
    }
}
