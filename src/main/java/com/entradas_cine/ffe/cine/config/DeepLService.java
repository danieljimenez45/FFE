package com.entradas_cine.ffe.cine.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Cliente REST para la API de traducción de DeepL.
 * Si la clave API no está configurada, devuelve el texto original sin modificar
 * para que la aplicación funcione correctamente en entornos sin clave (desarrollo local).
 */
@Slf4j
@Service
public class DeepLService {

    private static final Map<String, String> LOCALE_A_DEEPL = Map.of(
        "en", "EN-US",
        "fr", "FR",
        "de", "DE",
        "pt", "PT-PT"
    );

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${deepl.api.url:https://api-free.deepl.com/v2/translate}")
    private String apiUrl;

    @Value("${deepl.api.key:}")
    private String apiKey;

    @jakarta.annotation.PostConstruct
    void logEstado() {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("DeepL API key NO configurada — las traducciones automáticas están desactivadas.");
        } else {
            log.info("DeepL API key configurada ({} chars) — traducciones activas.", apiKey.length());
        }
    }

    /**
     * Traduce un texto del español al idioma indicado por el código de locale.
     *
     * @param texto        texto en español a traducir
     * @param targetLocale código BCP-47: en, fr, de, pt
     * @return texto traducido, o el original si la API no está disponible
     */
    @SuppressWarnings("unchecked")
    public String traducir(String texto, String targetLocale) {
        if (apiKey == null || apiKey.isBlank()) {
            log.debug("DeepL API key no configurada — se devuelve el texto original para locale '{}'", targetLocale);
            return texto;
        }

        String deeplLang = LOCALE_A_DEEPL.get(targetLocale);
        if (deeplLang == null) {
            log.warn("Locale '{}' no soportado por DeepL — se devuelve el texto original", targetLocale);
            return texto;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "DeepL-Auth-Key " + apiKey);

            Map<String, Object> body = Map.of(
                "text",        List.of(texto),
                "source_lang", "ES",
                "target_lang", deeplLang
            );

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object raw = response.getBody().get("translations");
                if (raw instanceof List<?> translations && !translations.isEmpty()) {
                    Object first = translations.getFirst();
                    if (first instanceof Map<?, ?> entry) {
                        Object text = entry.get("text");
                        if (text instanceof String translated) {
                            return translated;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error llamando a DeepL para locale '{}': {}", targetLocale, e.getMessage());
        }

        return texto; // fallback al original
    }
}
