package com.tecsup.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Error de configuración: valida el perfil ANTES de crear los beans.
 * Si PROFILE_ACTIVE trae un perfil que no existe (ej. "produccion"), la aplicación
 * se detiene con un mensaje claro en lugar de fallar más adelante por una
 * propiedad o una conexión faltante.
 */
public class ValidadorPerfil implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        validar(event.getEnvironment());
    }

    static void validar(ConfigurableEnvironment environment) {
        List<String> permitidos = Arrays.stream(
                        environment.getProperty("empresa.perfiles-permitidos", "dev,qa,test,prod").split(","))
                .map(String::trim)
                .toList();
        String[] activos = environment.getActiveProfiles();

        if (activos.length != 1) {
            throw new IllegalStateException("Debe activarse exactamente un perfil (" + String.join(", ", permitidos)
                    + "). Activos: " + Arrays.toString(activos));
        }
        if (!permitidos.contains(activos[0])) {
            throw new IllegalStateException("Perfil no válido: '" + activos[0] + "'. Use uno de: "
                    + String.join(", ", permitidos) + " (variable de entorno PROFILE_ACTIVE)");
        }
    }
}
