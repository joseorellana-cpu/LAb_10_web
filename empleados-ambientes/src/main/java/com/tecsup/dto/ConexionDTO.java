package com.tecsup.dto;

/**
 * Respuesta de GET /configuracion/conexion.
 * Ejemplo (dev): {"perfil":"dev","baseDatosEsperada":"empleados_dev","baseDatosConectada":"empleados_dev",
 *                 "conectado":true,"mensaje":"Conectado a la base de datos del perfil dev"}
 */
public record ConexionDTO(String perfil, String baseDatosEsperada, String baseDatosConectada,
                          boolean conectado, String mensaje) {
}
