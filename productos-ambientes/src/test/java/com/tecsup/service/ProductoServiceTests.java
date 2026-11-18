package com.tecsup.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Producto;
import com.tecsup.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTests {

	@Mock
	private ProductoRepository productoRepository;

	@InjectMocks
	private ProductoService productoService;

	@Test
	void listarDevuelveTodosLosProductos() {
		when(productoRepository.findAll()).thenReturn(Arrays.asList(crearProducto(1L), crearProducto(2L)));

		List<Producto> resultado = productoService.listar();

		assertThat(resultado).hasSize(2);
	}

	@Test
	void guardarPersisteYDevuelveElProducto() {
		Producto producto = crearProducto(null);
		when(productoRepository.save(producto)).thenReturn(crearProducto(99L));

		Producto guardado = productoService.guardar(producto);

		assertThat(guardado.getId()).isEqualTo(99L);
		verify(productoRepository).save(producto);
	}

	@Test
	void obtenerDevuelveElProductoCuandoExiste() {
		when(productoRepository.findById(1L)).thenReturn(Optional.of(crearProducto(1L)));

		Producto resultado = productoService.obtener(1L);

		assertThat(resultado.getNombre()).isEqualTo("Laptop");
	}

	@Test
	void obtenerLanzaResourceNotFoundExceptionCuandoNoExiste() {
		when(productoRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productoService.obtener(999L))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("999");
	}

	@Test
	void buscarPorNombreDelegaEnElRepositorio() {
		when(productoRepository.findByNombreContainingIgnoreCase("lap"))
				.thenReturn(Arrays.asList(crearProducto(1L)));

		List<Producto> resultado = productoService.buscarPorNombre("lap");

		assertThat(resultado).hasSize(1);
		assertThat(resultado.get(0).getNombre()).isEqualTo("Laptop");
	}

	private Producto crearProducto(Long id) {
		return new Producto(id, "Laptop", 2500.50, 10, "Tecnologia");
	}
}