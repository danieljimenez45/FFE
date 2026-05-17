package com.entradas_cine.ffe.cine.web.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class PeliculaPosterStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");
    private static final long MAX_BYTES = 5L * 1024 * 1024;
    private static final String CLOUDINARY_FOLDER = "peliculas";
    private static final String LOCAL_URL_PREFIX = "/images/peliculas/";

    @Value("${cloudinary.url:}")
    private String cloudinaryUrl;

    @Value("${app.peliculas.poster-dir:uploads/peliculas}")
    private String posterDirPath;

    private Cloudinary cloudinary;
    private Path posterDir;

    @PostConstruct
    void init() {
        if (cloudinaryUrl != null && !cloudinaryUrl.isBlank()) {
            this.cloudinary = new Cloudinary(cloudinaryUrl);
            log.info("PeliculaPosterStorage: Cloudinary configurado — imágenes en la nube.");
        } else {
            this.posterDir = Paths.get(posterDirPath).toAbsolutePath().normalize();
            try {
                Files.createDirectories(this.posterDir);
            } catch (IOException e) {
                throw new IllegalStateException(
                        "No se pudo crear el directorio de carátulas: " + posterDir, e);
            }
            log.info("PeliculaPosterStorage: disco local → {}", posterDir);
        }
    }

    public Path getPosterDir() {
        return posterDir;
    }

    /**
     * Almacena el fichero y devuelve la URL o ruta con la que se debe persistir en BD:
     * - Cloudinary: URL completa  (https://res.cloudinary.com/...)
     * - Local:      ruta web      (/images/peliculas/nombre.jpg)
     */
    public String store(MultipartFile file, String titulo) throws IOException {
        validate(file);

        if (cloudinary != null) {
            return uploadToCloudinary(file, titulo);
        } else {
            return saveToLocalDisk(file, titulo);
        }
    }

    public void requireValidPoster(String imagen) {
        if (imagen == null || imagen.isBlank()) {
            throw new IllegalArgumentException("admin.error.caratula.requerida");
        }
    }

    /**
     * Borra la imagen almacenada.
     * - Si empieza por https://res.cloudinary.com → borra en Cloudinary.
     * - Si empieza por /images/peliculas/ y el fichero existe en posterDir → borra en disco.
     * - Imágenes estáticas del classpath no se tocan.
     */
    public void deleteUploadedIfPresent(String imagen) {
        if (imagen == null || imagen.isBlank()) return;

        if (imagen.startsWith("https://res.cloudinary.com") ||
                imagen.startsWith("http://res.cloudinary.com")) {
            deleteFromCloudinary(imagen);
            return;
        }

        if (posterDir == null) return; // modo Cloudinary sin disco configurado

        // imagen es del tipo "/images/peliculas/nombre.jpg" → extraer solo el nombre
        String fileName = imagen.startsWith(LOCAL_URL_PREFIX)
                ? imagen.substring(LOCAL_URL_PREFIX.length())
                : imagen;

        Path target = posterDir.resolve(fileName).normalize();
        if (!target.startsWith(posterDir)) return; // fuera del directorio permitido

        try {
            if (Files.deleteIfExists(target)) {
                log.info("Carátula eliminada del disco: {}", fileName);
            }
        } catch (IOException e) {
            log.warn("No se pudo borrar la carátula {}: {}", fileName, e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Privados
    // -------------------------------------------------------------------------

    private String uploadToCloudinary(MultipartFile file, String titulo) throws IOException {
        String publicId = CLOUDINARY_FOLDER + "/" + slugify(titulo) + "-"
                + UUID.randomUUID().toString().substring(0, 8);

        @SuppressWarnings("unchecked")
        Map<String, Object> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "overwrite", true,
                        "resource_type", "image"
                ));

        String secureUrl = (String) result.get("secure_url");
        log.info("Carátula subida a Cloudinary: {}", secureUrl);
        return secureUrl;
    }

    private String saveToLocalDisk(MultipartFile file, String titulo) throws IOException {
        String extension = extensionFrom(file);
        String fileName = slugify(titulo) + "-"
                + UUID.randomUUID().toString().substring(0, 8) + extension;

        Path target = posterDir.resolve(fileName).normalize();
        if (!target.startsWith(posterDir)) {
            throw new SecurityException("Ruta de destino no válida");
        }

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        log.info("Carátula guardada en disco: {}", fileName);
        return LOCAL_URL_PREFIX + fileName;
    }

    private void deleteFromCloudinary(String secureUrl) {
        String publicId = extractPublicId(secureUrl);
        if (publicId == null) {
            log.warn("No se pudo extraer el public_id de la URL: {}", secureUrl);
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Carátula eliminada de Cloudinary: {}", publicId);
        } catch (Exception e) {
            log.warn("No se pudo borrar la carátula en Cloudinary ({}): {}", publicId, e.getMessage());
        }
    }

    /** Extrae el public_id de una URL segura de Cloudinary. */
    private String extractPublicId(String url) {
        int uploadIdx = url.indexOf("/upload/");
        if (uploadIdx < 0) return null;
        String afterUpload = url.substring(uploadIdx + 8);
        // quitar prefijo de versión (v1234567890/)
        afterUpload = afterUpload.replaceFirst("^v\\d+/", "");
        // quitar extensión
        int dotIdx = afterUpload.lastIndexOf('.');
        return dotIdx >= 0 ? afterUpload.substring(0, dotIdx) : afterUpload;
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
        return "image/png".equals(file.getContentType()) ? ".png" : ".jpg";
    }

    private String slugify(String titulo) {
        String normalized = Normalizer.normalize(titulo, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "");
        if (normalized.isBlank()) return "pelicula";
        return normalized.length() > 40 ? normalized.substring(0, 40) : normalized;
    }
}
