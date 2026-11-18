package com.tecsup.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tecsup.dto.ProductoDTO;
import com.tecsup.model.Producto;
import com.tecsup.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

	private final ProductoService productoService;

	@Autowired
	public ProductoController(ProductoService productoService) {
		this.productoService = productoService;
	}

	@GetMapping
	public List<Producto> listar() {
		return productoService.listar();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Producto> obtener(@PathVariable Long id) {
		return ResponseEntity.ok(productoService.obtener(id));
	}

	@GetMapping("/buscar")
	public List<Producto> buscarPorNombre(@RequestParam("nombre") String nombre) {
		return productoService.buscarPorNombre(nombre);
	}

	@PostMapping
	public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoDTO dto) {
		Producto producto = new Producto(null, dto.getNombre(), dto.getPrecio(), dto.getStock(), dto.getCategoria());
		Producto guardado = productoService.guardar(producto);
		return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
		Producto existente = productoService.obtener(id);
		existente.setNombre(dto.getNombre());
		existente.setPrecio(dto.getPrecio());
		existente.setStock(dto.getStock());
		existente.setCategoria(dto.getCategoria());
		return ResponseEntity.ok(productoService.actualizar(existente));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
		productoService.eliminar(id);
		return ResponseEntity.ok(Map.of("mensaje", "Producto con id " + id + " eliminado correctamente"));
	}
}