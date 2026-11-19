package com.tecsup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecsup.dto.ConexionDTO;
import com.tecsup.dto.ConfiguracionDTO;
import com.tecsup.service.ConfiguracionService;

/**
 * GET /configuracion          -> ambiente, perfil, puerto y base de datos configurados.
 * GET /configuracion/conexion -> base de datos a la que la aplicación está conectada realmente.
 */
@RestController
@RequestMapping("/configuracion")
public class ConfiguracionController {

    private final ConfiguracionService service;

    public ConfiguracionController(ConfiguracionService service) {
        this.service = service;
    }

    @GetMapping
    public ConfiguracionDTO configuracion() {
        return service.configuracion();
    }

    @GetMapping("/conexion")
    public ResponseEntity<ConexionDTO> conexion() {
        ConexionDTO conexion = service.verificarConexion();
        HttpStatus status = conexion.conectado() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(conexion);
    }
}
