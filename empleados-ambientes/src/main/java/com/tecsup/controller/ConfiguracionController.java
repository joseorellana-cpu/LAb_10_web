package com.tecsup.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tecsup.dto.ConfiguracionDTO;

/**
 * GET /configuracion -> muestra el ambiente con el que arrancó la aplicación.
 * Ejemplo (dev): {"ambiente":"DESARROLLO","perfil":"dev","puerto":"8080","baseDatos":"empleados_dev"}
 */
@RestController
public class ConfiguracionController {

    private final Environment environment;

    @Value("${empresa.ambiente}")
    private String ambiente;

    @Value("${server.port:8080}")
    private String puerto;

    @Value("${spring.datasource.url}")
    private String urlBaseDatos;

    public ConfiguracionController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/configuracion")
    public ConfiguracionDTO configuracion() {
        String perfil = Arrays.stream(environment.getActiveProfiles())
                .findFirst()
                .orElse("default");
        return new ConfiguracionDTO(ambiente, perfil, puerto, nombreBaseDatos(urlBaseDatos));
    }

    // jdbc:mysql://localhost:3306/empleados_dev?param=x  ->  empleados_dev
    static String nombreBaseDatos(String url) {
        String sinParametros = url.split("[?;]")[0];
        int inicio = Math.max(sinParametros.lastIndexOf('/'), sinParametros.lastIndexOf(':'));
        return sinParametros.substring(inicio + 1);
    }
}
