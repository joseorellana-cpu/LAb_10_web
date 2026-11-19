package com.tecsup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tecsup.config.ValidadorPerfil;

@SpringBootApplication
public class EmpleadosAmbientesApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(EmpleadosAmbientesApplication.class);
        // Verifica que PROFILE_ACTIVE sea dev, qa, test o prod antes de iniciar
        app.addListeners(new ValidadorPerfil());
        app.run(args);
    }
}
