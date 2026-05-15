package com.entradas_cine.ffe.cine.web.controllers;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.entradas_cine.ffe.cine.rest.facturas.dto.EntradaLineaDto;
import com.entradas_cine.ffe.cine.rest.facturas.dto.FacturaResponseDto;
import com.entradas_cine.ffe.cine.rest.facturas.services.FacturaService;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FacturaPdfController {

    private final FacturaService facturaService;
    private final SesionRepository sesionRepository;

    /** Página HTML de confirmación con iframe de preview del PDF. */
    @GetMapping("/facturas/{id}/pdf")
    @Transactional(readOnly = true)
    public String pdfPage(@PathVariable Long id, Model model) {
        FacturaResponseDto factura = facturaService.findById(id);
        model.addAttribute("factura", factura);
        return "facturas/confirmacion";
    }

    /**
     * Genera y sirve el PDF.
     * - Sin parámetro: inline (preview en el iframe).
     * - Con ?dl=true:  attachment (descarga directa).
     */
    @GetMapping("/facturas/{id}/pdf/descargar")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean dl) {

        FacturaResponseDto factura = facturaService.findById(id);

        // Recopilar info de sesiones (una sola consulta por sesión)
        Map<Long, SesionInfo> sesionInfoMap = new HashMap<>();
        for (EntradaLineaDto entrada : factura.getEntradas()) {
            sesionInfoMap.computeIfAbsent(entrada.getIdSesion(), sesionId ->
                sesionRepository.findById(sesionId)
                    .map(s -> new SesionInfo(
                            s.getPelicula().getTitulo(),
                            s.getFecha(),
                            s.getHorario().getDisplayName(),
                            s.getSala().getDisplayName()))
                    .orElse(new SesionInfo("—", null, "—", "—"))
            );
        }

        byte[] pdfBytes = generarPdf(factura, sesionInfoMap);
        String disposition = dl ? "attachment" : "inline";
        String filename = "factura-" + factura.getCodigoFactura() + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", disposition + "; filename=\"" + filename + "\"")
                .body(pdfBytes);
    }

    // -------------------------------------------------------------------------

    private record SesionInfo(String titulo, LocalDate fecha, String horario, String sala) {}

    private static final Color COLOR_DARK   = new Color(11, 26, 51);
    private static final Color COLOR_ACCENT = new Color(207, 79, 0);
    private static final Color COLOR_LIGHT  = new Color(240, 240, 240);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private byte[] generarPdf(FacturaResponseDto factura, Map<Long, SesionInfo> sesiones) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.A4, 45, 45, 55, 55);

        try {
            PdfWriter.getInstance(doc, out);
            doc.open();

            // ---- CABECERA ----
            Font fCinema = new Font(Font.HELVETICA, 24, Font.BOLD, Color.WHITE);
            Font fSubtitle = new Font(Font.HELVETICA, 11, Font.NORMAL, COLOR_ACCENT);
            Font fLabel  = new Font(Font.HELVETICA, 9,  Font.BOLD,   COLOR_DARK);
            Font fValue  = new Font(Font.HELVETICA, 9,  Font.NORMAL, COLOR_DARK);
            Font fTitle  = new Font(Font.HELVETICA, 13, Font.BOLD,   COLOR_DARK);
            Font fWhite  = new Font(Font.HELVETICA, 9,  Font.NORMAL, Color.WHITE);

            PdfPTable header = new PdfPTable(1);
            header.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell(new Phrase("FFE CINEMA", fCinema));
            headerCell.setBackgroundColor(COLOR_DARK);
            headerCell.setPadding(16);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            header.addCell(headerCell);

            PdfPCell subCell = new PdfPCell(new Phrase("Factura de compra de entradas", fSubtitle));
            subCell.setBackgroundColor(COLOR_DARK);
            subCell.setPaddingBottom(14);
            subCell.setPaddingLeft(16);
            subCell.setBorder(Rectangle.NO_BORDER);
            header.addCell(subCell);
            doc.add(header);

            doc.add(Chunk.NEWLINE);

            // ---- DATOS FACTURA + CLIENTE ----
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1f, 1f});

            // Columna factura
            PdfPCell c1 = new PdfPCell();
            c1.setBorderColor(COLOR_LIGHT);
            c1.setPadding(10);
            c1.addElement(new Paragraph("DATOS DE LA FACTURA", fLabel));
            c1.addElement(spacer(4));
            c1.addElement(field("Código:", factura.getCodigoFactura(), fLabel, fValue));
            c1.addElement(field("Nº Factura:", String.valueOf(factura.getNumeroFactura()), fLabel, fValue));
            c1.addElement(field("Fecha:", LocalDate.now().format(DATE_FMT), fLabel, fValue));
            infoTable.addCell(c1);

            // Columna cliente
            PdfPCell c2 = new PdfPCell();
            c2.setBorderColor(COLOR_LIGHT);
            c2.setPadding(10);
            c2.addElement(new Paragraph("DATOS DEL CLIENTE", fLabel));
            c2.addElement(spacer(4));
            c2.addElement(field("Nombre:", factura.getNombre() + " " + factura.getApellidos(), fLabel, fValue));
            c2.addElement(field("Email:", factura.getEmail(), fLabel, fValue));
            infoTable.addCell(c2);

            doc.add(infoTable);
            doc.add(spacer(12));

            // ---- ENTRADAS ----
            doc.add(new Paragraph("Entradas", fTitle));
            doc.add(spacer(6));

            for (EntradaLineaDto entrada : factura.getEntradas()) {
                SesionInfo si = sesiones.getOrDefault(entrada.getIdSesion(),
                        new SesionInfo("—", null, "—", "—"));

                // QR por entrada: combina factura + fila + numero de butaca.
                Image qrImage = null;
                try {
                    String qrPayload = "factura=" + factura.getCodigoFactura()
                            + ";fila=" + entrada.getFila()
                            + ";butaca=" + entrada.getNumero();
                    String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?data="
                            + URLEncoder.encode(qrPayload, StandardCharsets.UTF_8)
                            + "&size=130x130&margin=4";
                    qrImage = Image.getInstance(new URL(qrUrl));
                    qrImage.scaleToFit(90, 90);
                } catch (Exception e) {
                    log.warn("No se pudo obtener el QR para factura {} fila {} butaca {}: {}",
                            factura.getCodigoFactura(), entrada.getFila(), entrada.getNumero(), e.getMessage());
                }

                PdfPTable entradaTable = new PdfPTable(qrImage != null ? 2 : 1);
                entradaTable.setWidthPercentage(100);
                if (qrImage != null) {
                    entradaTable.setWidths(new float[]{3f, 1f});
                }

                // Info entrada
                PdfPCell infoCell = new PdfPCell();
                infoCell.setBorderColor(COLOR_LIGHT);
                infoCell.setBackgroundColor(new Color(252, 252, 252));
                infoCell.setPadding(10);

                Paragraph movieTitle = new Paragraph(si.titulo(), new Font(Font.HELVETICA, 12, Font.BOLD, COLOR_DARK));
                infoCell.addElement(movieTitle);
                infoCell.addElement(spacer(4));
                infoCell.addElement(field("Fecha:",    si.fecha() != null ? si.fecha().format(DATE_FMT) : "—", fLabel, fValue));
                infoCell.addElement(field("Horario:",  si.horario(), fLabel, fValue));
                infoCell.addElement(field("Sala:",     si.sala(),    fLabel, fValue));
                infoCell.addElement(field("Fila:",     String.valueOf(entrada.getFila()),   fLabel, fValue));
                infoCell.addElement(field("Butaca:",   String.valueOf(entrada.getNumero()), fLabel, fValue));
                infoCell.addElement(field("Precio:",
                        String.format("%.2f €", entrada.getPrecio() != null ? entrada.getPrecio() : 0f),
                        fLabel, fValue));
                entradaTable.addCell(infoCell);

                // QR
                if (qrImage != null) {
                    PdfPCell qrCell = new PdfPCell(qrImage);
                    qrCell.setBorderColor(COLOR_LIGHT);
                    qrCell.setBackgroundColor(new Color(252, 252, 252));
                    qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    qrCell.setPadding(8);
                    entradaTable.addCell(qrCell);
                }

                doc.add(entradaTable);

                // Código QR debajo (texto pequeño)
                String qrCodeText = factura.getCodigoFactura()
                    + " | F" + entrada.getFila()
                    + "-B" + entrada.getNumero();
                Paragraph qrCode = new Paragraph(qrCodeText,
                        new Font(Font.COURIER, 7, Font.NORMAL, new Color(120, 120, 120)));
                qrCode.setAlignment(Element.ALIGN_RIGHT);
                doc.add(qrCode);
                doc.add(spacer(8));
            }

            // ---- TOTAL ----
            PdfPTable totalTable = new PdfPTable(1);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell totalCell = new PdfPCell(
                    new Phrase("TOTAL: " + String.format("%.2f €", factura.getTotal() != null ? factura.getTotal() : 0f),
                            new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE)));
            totalCell.setBackgroundColor(COLOR_ACCENT);
            totalCell.setPadding(10);
            totalCell.setBorder(Rectangle.NO_BORDER);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalCell);
            doc.add(totalTable);

            doc.add(spacer(20));

            // ---- PIE ----
            Font fFooter = new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(140, 140, 140));
            Paragraph footer = new Paragraph("Este documento es tu entrada. Preséntalo en taquilla.", fFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            doc.add(footer);

        } catch (Exception e) {
            log.error("Error generando PDF para factura {}", factura.getNumeroFactura(), e);
        } finally {
            doc.close();
        }

        return out.toByteArray();
    }

    private Chunk spacer(int height) {
        return new Chunk(" \n".repeat(height / 6 + 1));
    }

    private Paragraph field(String label, String value, Font fLabel, Font fValue) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + " ", fLabel));
        p.add(new Chunk(value, fValue));
        return p;
    }
}
