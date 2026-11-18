package com.tecsup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tecsup.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

	/**
	 * Busca productos cuyo nombre contenga el texto indicado, sin distinguir
	 * mayúsculas de minúsculas.
	 */
	List<Producto> findByNombreContainingIgnoreCase(String nombre);
}