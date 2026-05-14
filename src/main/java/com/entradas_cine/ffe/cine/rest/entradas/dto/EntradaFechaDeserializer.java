package com.entradas_cine.ffe.cine.rest.entradas.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EntradaFechaDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMAT_DD_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String valor = parser.getValueAsString();

        if (valor == null || valor.isBlank()) {
            return null;
        }

        String fechaTexto = valor.trim();

        try {
            return LocalDateTime.parse(fechaTexto, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException ignored) {
        }

        try {
            return OffsetDateTime.parse(fechaTexto, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime();
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDate.parse(fechaTexto, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        } catch (DateTimeParseException ignored) {
        }

        try {
            return LocalDate.parse(fechaTexto, FORMAT_DD_MM_YYYY).atStartOfDay();
        } catch (DateTimeParseException ignored) {
        }

        throw new IOException(
                "Formato de fecha no valido. Usa uno de estos formatos: "
                        + "yyyy-MM-dd'T'HH:mm:ss, yyyy-MM-dd'T'HH:mm:ssXXX, yyyy-MM-dd o dd-MM-yyyy"
        );
    }
}
