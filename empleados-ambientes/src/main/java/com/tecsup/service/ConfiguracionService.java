package com.tecsup.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tecsup.dto.ConexionDTO;
import com.tecsup.dto.ConfiguracionDTO;

/**
 * Lee la configuración del ambiente activo de dos formas:
 *  - @Value: inyecta una propiedad concreta (empresa.ambiente).
 *  - Environment: consulta propiedades y perfiles en tiempo de ejecución.
 */
@Service
public class ConfiguracionService {

    private final Environment environment;
    private final JdbcTemplate jdbcTemplate;

    // Propiedad personalizada inyectada con @Value (definida en application-<perfil>.properties)
    @Value("${empresa.ambiente}")
    private String ambiente;

    public ConfiguracionService(Environment environment, JdbcTemplate jdbcTemplate) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
    }

    public ConfiguracionDTO configuracion() {
        return new ConfiguracionDTO(ambiente, perfilActivo(), puerto(), baseDatosConfigurada());
    }

    /**
     * Pregunta a MySQL a qué base de datos está conectada realmente la aplicación
     * y la compara con la que indica el perfil activo.
     */
    public ConexionDTO verificarConexion() {
        String esperada = baseDatosConfigurada();
        try {
            String conectada = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
            boolean coincide = esperada.equalsIgnoreCase(conectada);
            String mensaje = coincide
                    ? "Conectado a la base de datos del perfil " + perfilActivo()
                    : "La conexión no corresponde al perfil activo";
            return new ConexionDTO(perfilActivo(), esperada, conectada, coincide, mensaje);
        } catch (DataAccessException ex) {
            return new ConexionDTO(perfilActivo(), esperada, null, false,
                    "No se pudo conectar a la base de datos: " + ex.getMostSpecificCause().getMessage());
        }
    }

    // ----- lecturas con Environment

    private String perfilActivo() {
        return Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("default");
    }

    private String puerto() {
        // local.server.port = puerto real en el que arrancó Tomcat
        return environment.getProperty("local.server.port", environment.getProperty("server.port", "8080"));
    }

    private String baseDatosConfigurada() {
        return nombreBaseDatos(environment.getRequiredProperty("spring.datasource.url"));
    }

    // jdbc:mysql://localhost:3306/empleados_dev?param=x  ->  empleados_dev
    static String nombreBaseDatos(String url) {
        String sinParametros = url.split("[?;]")[0];
        int inicio = Math.max(sinParametros.lastIndexOf('/'), sinParametros.lastIndexOf(':'));
        return sinParametros.substring(inicio + 1);
    }
}
