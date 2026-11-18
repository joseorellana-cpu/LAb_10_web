package com.tecsup;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmpleadoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String EMPLEADO = """
            {"nombres":"Ana","apellidos":"Quispe Rojas","email":"ana.quispe@techsolutions.pe","sueldo":3500.50}
            """;

    @Test
    void crudCompletoDeEmpleado() throws Exception {
        // POST
        String creado = mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON).content(EMPLEADO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombres").value("Ana"))
                .andReturn().getResponse().getContentAsString();
        Matcher m = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(creado);
        m.find();
        long id = Long.parseLong(m.group(1));

        // GET lista y por id
        mockMvc.perform(get("/empleados")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").exists());
        mockMvc.perform(get("/empleados/" + id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.apellidos").value("Quispe Rojas"));

        // PUT
        mockMvc.perform(put("/empleados/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content(EMPLEADO.replace("3500.50", "4200.00")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sueldo").value(4200.00));

        // DELETE y verificar 404
        mockMvc.perform(delete("/empleados/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/empleados/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Empleado no encontrado con id: " + id));
    }

    @Test
    void crearEmpleadoInvalidoDevuelve400() throws Exception {
        mockMvc.perform(post("/empleados").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombres\":\"\",\"apellidos\":\"X\",\"email\":\"no-es-email\",\"sueldo\":-5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombres").value("Los nombres son obligatorios"))
                .andExpect(jsonPath("$.email").value("El email no tiene un formato válido"))
                .andExpect(jsonPath("$.sueldo").value("El sueldo debe ser mayor que cero"));
    }

    @Test
    void configuracionMuestraAmbienteYBaseDeDatos() throws Exception {
        mockMvc.perform(get("/configuracion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ambiente").value("JUNIT"))
                .andExpect(jsonPath("$.baseDatos").value("empleados_junit"))
                .andExpect(jsonPath("$.puerto").exists())
                .andExpect(jsonPath("$.perfil").exists());
    }
}
