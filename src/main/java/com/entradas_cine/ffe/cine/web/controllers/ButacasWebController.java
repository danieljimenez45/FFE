package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.rest.entradas.dto.ButacaOcupadaResponseDto;
import com.entradas_cine.ffe.cine.rest.entradas.services.EntradaService;
import com.entradas_cine.ffe.cine.rest.sesiones.models.Sesion;
import com.entradas_cine.ffe.cine.rest.sesiones.repositories.SesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ButacasWebController {

    private final SesionRepository sesionRepository;
    private final EntradaService entradaService;

    @GetMapping("/sesiones/{id}/butacas")
    @Transactional(readOnly = true)
    public String seleccionarButacas(@PathVariable Long id, Model model) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sesion no encontrada"));

        List<ButacaOcupadaResponseDto> ocupadas = entradaService.findButacasOcupadasBySesion(id);
        Set<String> ocupadasKeys = ocupadas.stream()
                .map(ButacasWebController::seatKey)
                .collect(Collectors.toSet());

        List<FilaButacasView> filasButacas = IntStream.rangeClosed(1, sesion.getSala().getMaxFilas())
                .mapToObj(numeroFila -> new FilaButacasView(
                        numeroFila,
                        IntStream.rangeClosed(1, sesion.getSala().getMaxNumeros())
                                .mapToObj(numeroButaca -> new ButacaView(
                                        numeroFila,
                                        numeroButaca,
                                        seatKey(numeroFila, numeroButaca),
                                        ocupadasKeys.contains(seatKey(numeroFila, numeroButaca))
                                ))
                                .toList()
                ))
                .toList();

        model.addAttribute("sesion", sesion);
        model.addAttribute("filasButacas", filasButacas);
        model.addAttribute("maxFilas", sesion.getSala().getMaxFilas());
        model.addAttribute("maxNumeros", sesion.getSala().getMaxNumeros());
        model.addAttribute("butacasOcupadas", ocupadas);
        return "butacas/selector";
    }

    private static String seatKey(ButacaOcupadaResponseDto butaca) {
        return seatKey(butaca.getFila(), butaca.getNumero());
    }

    private static String seatKey(int fila, int numero) {
        return fila + "-" + numero;
    }

    public static class FilaButacasView {
        private final int numeroFila;
        private final List<ButacaView> butacas;

        public FilaButacasView(int numeroFila, List<ButacaView> butacas) {
            this.numeroFila = numeroFila;
            this.butacas = butacas;
        }

        public int getNumeroFila() {
            return numeroFila;
        }

        public List<ButacaView> getButacas() {
            return butacas;
        }
    }

    public static class ButacaView {
        private final int fila;
        private final int numero;
        private final String key;
        private final boolean ocupada;

        public ButacaView(int fila, int numero, String key, boolean ocupada) {
            this.fila = fila;
            this.numero = numero;
            this.key = key;
            this.ocupada = ocupada;
        }

        public int getFila() {
            return fila;
        }

        public int getNumero() {
            return numero;
        }

        public String getKey() {
            return key;
        }

        public boolean isOcupada() {
            return ocupada;
        }
    }
}