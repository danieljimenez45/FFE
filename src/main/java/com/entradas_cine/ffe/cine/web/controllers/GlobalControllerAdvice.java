package com.entradas_cine.ffe.cine.web.controllers;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("appName")
    public String getAppName() {
        return "Entradas Cine FFE";
    }
}
