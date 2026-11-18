package com.tecsup.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void crearProducto_devuelve201ConDatosGuardados() throws Exception {
		mockMvc.perform(post("/api/productos").header("Usuario", "Jose").header("Rol", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpoJson("Laptop HP", "2500.50", "10", "Tecnologia")))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.nombre").value("Laptop HP"))
			.andExpect(jsonPath("$.precio").value(2500.5))
			.andExpect(jsonPath("$.stock").value(10))
			.andExpect(jsonPath("$.categoria").value("Tecnologia"));
	}

	@Test
	void crearProductoConDatosInvalidos_devuelve400ConErroresDeValidacion() throws Exception {
		mockMvc.perform(post("/api/productos").header("Usuario", "Jose").header("Rol", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpoJson("", "-10", "-1", "")))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.nombre").value(containsString("no puede estar vacío")))
			.andExpect(jsonPath("$.precio").value(containsString("mayor que cero")))
			.andExpect(jsonPath("$.stock").value(containsString("no puede ser negativo")))
			.andExpect(jsonPath("$.categoria").value(containsString("no puede estar vacía")));
	}

	@Test
	void obtenerProductoInexistente_devuelve404ConMensaje() throws Exception {
		mockMvc.perform(get("/api/productos/999999999").header("Usuario", "Jose").header("Rol", "ADMIN"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.mensaje").value(containsString("no encontrado")));
	}

	@Test
	void buscarProductosPorNombre_devuelveCoincidenciasSinDistinguirMayusculas() throws Exception {
		mockMvc.perform(post("/api/productos").header("Usuario", "Jose").header("Rol", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpoJson("Teclado Mecanico", "150.75", "8", "Accesorios")))
			.andExpect(status().isCreated());

		mockMvc.perform(get("/api/productos/buscar").header("Usuario", "Jose").header("Rol", "ADMIN").param("nombre", "MECANI"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].nombre").value("Teclado Mecanico"));
	}

	@Test
	void buscarProductosPorNombreSinResultados_devuelveListaVacia() throws Exception {
		mockMvc.perform(get("/api/productos/buscar").header("Usuario", "Jose").header("Rol", "ADMIN").param("nombre", "productoinexistente999"))
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));
	}

	@Test
	void listarProductos_devuelveListaNoVacia() throws Exception {
		mockMvc.perform(get("/api/productos").header("Usuario", "Jose").header("Rol", "ADMIN"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray());
	}

	@Test
	void actualizarProducto_devuelve200ConLosNuevosDatos() throws Exception {
		long id = crearProductoYDevolverId(cuerpoJson("Monitor Samsung", "1200.00", "4", "Tecnologia"));

		mockMvc.perform(put("/api/productos/" + id).header("Usuario", "Jose").header("Rol", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)
				.content(cuerpoJson("Monitor Samsung Curvo", "1350.00", "6", "Monitores")))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(id))
			.andExpect(jsonPath("$.nombre").value("Monitor Samsung Curvo"))
			.andExpect(jsonPath("$.categoria").value("Monitores"));
	}

	@Test
	void eliminarProducto_devuelve200YElProductoYaNoExiste() throws Exception {
		long id = crearProductoYDevolverId(cuerpoJson("Impresora Epson", "800.00", "3", "Perifericos"));

		mockMvc.perform(delete("/api/productos/" + id).header("Usuario", "Jose").header("Rol", "ADMIN"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.mensaje").value(containsString("eliminado correctamente")));

		mockMvc.perform(get("/api/productos/" + id).header("Usuario", "Jose").header("Rol", "ADMIN"))
			.andExpect(status().isNotFound());
	}

	private long crearProductoYDevolverId(String json) throws Exception {
		MvcResult resultado = mockMvc.perform(post("/api/productos").header("Usuario", "Jose").header("Rol", "ADMIN")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isCreated())
			.andReturn();

		String cuerpo = resultado.getResponse().getContentAsString(StandardCharsets.UTF_8);
		Matcher matcher = Pattern.compile("\"id\":(\\d+)").matcher(cuerpo);
		if (!matcher.find()) {
			throw new IllegalStateException("No se pudo leer el id del producto creado: " + cuerpo);
		}
		return Long.parseLong(matcher.group(1));
	}

	private String cuerpoJson(String nombre, String precio, String stock, String categoria) {
		return "{"
				+ "\"nombre\":\"" + nombre + "\","
				+ "\"precio\":" + precio + ","
				+ "\"stock\":" + stock + ","
				+ "\"categoria\":\"" + categoria + "\""
				+ "}";
	}
}