package com.tecsup.service;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;

@Service
public class ProductoService {

	private final ProductoRepository productoRepository;

	@Autowired
	public ProductoService(ProductoRepository productoRepository) {
		this.productoRepository = productoRepository;
	}

	public List<Producto> listar() {
		return productoRepository.findAll();
	}

	public Producto guardar(@Valid Producto producto) {
		return productoRepository.save(producto);
	}
	public Producto obtener(Long id) {
		return productoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Producto con id " + id + " no encontrado"));
	}

	public void eliminar(Long id) {
		obtener(id);
		productoRepository.deleteById(id);
	}

	public List<Producto> buscarPorNombre(String nombre) {
		return productoRepository.findByNombreContainingIgnoreCase(nombre);
	}
	public Producto actualizar(@Valid Producto existente) {
		Producto actual = obtener(existente.getId());
		actual.setNombre(existente.getNombre());
		actual.setCategoria(existente.getCategoria());
		actual.setStock(existente.getStock());
		return productoRepository.save(actual);
	}
}