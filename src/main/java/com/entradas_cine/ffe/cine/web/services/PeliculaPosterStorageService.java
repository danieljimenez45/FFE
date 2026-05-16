package com.entradas_cine.ffe.cine.web.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class PeliculaPosterStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_BYTES = 5L * 1024 * 1024;

    private final Path posterDir;

    public PeliculaPosterStorageService(
            @Value("${app.peliculas.poster-dir:uploads/peliculas}") String posterDirPath) {
        this.posterDir = Paths.get(posterDirPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.posterDir);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo crear el directorio de carátulas: " + this.posterDir, e);
        }
        log.info("Carátulas de películas en: {}", this.posterDir);
    }

    public Path getPosterDir() {
        return posterDir;
    }

    public String store(MultipartFile file, String titulo) throws IOException {
        validate(file);

        String extension = extensionFrom(file);
        String baseName = slugify(titulo);
        String fileName = baseName + "-" + UUID.randomUUID().toString().substring(0, 8) + extension;

        Path target = posterDir.resolve(fileName).normalize();
        if (!target.startsWith(posterDir)) {
            throw new SecurityException("Ruta de destino no válida");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Carátula guardada: {}", fileName);
        return fileName;
    }

    public void requireValidPoster(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("admin.error.caratula.requerida");
        }
    }

    /** Borra el fichero solo si está en la carpeta de subidas (no toca las del classpath/static). */
    public void deleteUploadedIfPresent(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return;
        }
        Path target = posterDir.resolve(fileName).normalize();
        if (!target.startsWith(posterDir)) {
            return;
        }
        try {
            if (Files.deleteIfExists(target)) {
                log.info("Carátula eliminada del disco: {}", fileName);
            }
        } catch (IOException e) {
            log.warn("No se pudo borrar la carátula {}: {}", fileName, e.getMessage());
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("admin.error.caratula.requerida");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("admin.error.caratula.invalida");
        }
        if (file.getSize() > MAX_BYTES) {
            throw new IllegalArgumentException("admin.error.caratula.tamano");
        }
    }

    private String extensionFrom(MultipartFile file) {
        String contentType = file.getContentType();
        if ("image/png".equals(contentType)) {
            return ".png";
        }
        return ".jpg";
    }

    private String slugify(String titulo) {
        String normalized = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "");
        if (normalized.isBlank()) {
            return "pelicula";
        }
        return normalized.length() > 40 ? normalized.substring(0, 40) : normalized;
    }
}
