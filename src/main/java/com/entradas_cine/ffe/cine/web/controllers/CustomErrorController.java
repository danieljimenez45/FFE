package com.entradas_cine.ffe.cine.web.controllers;

import com.entradas_cine.ffe.cine.web.services.I18nService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {

    private final I18nService i18nService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        String errorTitle   = i18nService.getMessage("error.500");
        String errorMessage = i18nService.getMessage("error.general");
        String errorCode    = "500";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorCode = String.valueOf(statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorTitle   = i18nService.getMessage("error.404");
                errorMessage = i18nService.getMessage("error.404.message");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorTitle   = i18nService.getMessage("error.403");
                errorMessage = i18nService.getMessage("error.403.message");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorTitle   = i18nService.getMessage("error.401");
                errorMessage = i18nService.getMessage("error.401.message");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorTitle   = i18nService.getMessage("error.500");
                errorMessage = i18nService.getMessage("error.500.message");
            } else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
                errorTitle   = i18nService.getMessage("error.503");
                errorMessage = i18nService.getMessage("error.503.message");
            }
        }

        model.addAttribute("errorTitle",   errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorCode",    errorCode);

        return "error";
    }
}
