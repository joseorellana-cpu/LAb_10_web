package com.tecsup.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class ValidadorPerfilTests {

    private MockEnvironment ambiente(String... perfiles) {
        MockEnvironment env = new MockEnvironment();
        env.setActiveProfiles(perfiles);
        return env;
    }

    @Test
    void aceptaLosCuatroPerfiles() {
        for (String perfil : new String[] {"dev", "qa", "test", "prod"}) {
            assertDoesNotThrow(() -> ValidadorPerfil.validar(ambiente(perfil)));
        }
    }

    @Test
    void rechazaPerfilInexistente() {
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> ValidadorPerfil.validar(ambiente("produccion")));
        assertTrue(ex.getMessage().contains("Perfil no válido: 'produccion'"));
    }

    @Test
    void rechazaDosPerfilesALaVez() {
        assertThrows(IllegalStateException.class, () -> ValidadorPerfil.validar(ambiente("dev", "prod")));
    }
}
