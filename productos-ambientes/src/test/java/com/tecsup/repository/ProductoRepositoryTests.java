package com.tecsup.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.tecsup.model.Producto;

@DataJpaTest
class ProductoRepositoryTests {

	@Autowired
	private ProductoRepository productoRepository;

	@Test
	void findByNombreContainingIgnoreCaseEncuentraCoincidenciasParciales() {
		productoRepository.save(new Producto(null, "Laptop HP", 2500.0, 10, "Tecnologia"));
		productoRepository.save(new Producto(null, "Laptop Lenovo", 3200.0, 5, "Tecnologia"));
		productoRepository.save(new Producto(null, "Mouse", 50.0, 20, "Accesorios"));

		List<Producto> resultado = productoRepository.findByNombreContainingIgnoreCase("laptop");

		assertThat(resultado).hasSize(2);
		assertThat(resultado).extracting(Producto::getNombre)
				.contains("Laptop HP", "Laptop Lenovo");
	}

	@Test
	void findByNombreContainingIgnoreCaseDevuelveListaVaciaSinCoincidencias() {
		productoRepository.save(new Producto(null, "Laptop", 2500.0, 10, "Tecnologia"));

		List<Producto> resultado = productoRepository.findByNombreContainingIgnoreCase("nohayproducto");

		assertThat(resultado).isEmpty();
	}
}