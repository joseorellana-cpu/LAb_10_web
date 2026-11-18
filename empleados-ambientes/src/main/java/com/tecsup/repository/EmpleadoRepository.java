package com.tecsup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tecsup.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
}
