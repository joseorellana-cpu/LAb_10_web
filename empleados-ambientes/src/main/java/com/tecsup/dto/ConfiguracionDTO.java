package com.tecsup.dto;

/**
 * Respuesta de GET /configuracion.
 */
public record ConfiguracionDTO(String ambiente, String perfil, String puerto, String baseDatos) {
}
