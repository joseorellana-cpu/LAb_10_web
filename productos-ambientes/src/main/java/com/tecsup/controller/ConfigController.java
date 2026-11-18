package com.tecsup.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint para verificar qué ambiente (perfil) está activo.
 * DEV  -> http://localhost:8080/config
 * QA   -> http://localhost:8082/config
 * PROD -> http://localhost:8083/config
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${empresa.ambiente}")
    private String ambiente;

    @GetMapping
    public String obtenerAmbiente() {
        return "Ambiente activo: " + ambiente;
    }
}
