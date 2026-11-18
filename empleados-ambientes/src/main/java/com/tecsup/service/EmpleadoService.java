package com.tecsup.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tecsup.dto.EmpleadoDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Empleado;
import com.tecsup.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
    }

    public List<Empleado> listar() {
        return repo.findAll();
    }

    public Empleado obtener(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado con id: " + id));
    }

    public Empleado crear(EmpleadoDTO dto) {
        Empleado empleado = new Empleado();
        copiar(dto, empleado);
        return repo.save(empleado);
    }

    public Empleado actualizar(Long id, EmpleadoDTO dto) {
        Empleado empleado = obtener(id);
        copiar(dto, empleado);
        return repo.save(empleado);
    }

    public void eliminar(Long id) {
        repo.delete(obtener(id));
    }

    private void copiar(EmpleadoDTO dto, Empleado empleado) {
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setEmail(dto.getEmail());
        empleado.setSueldo(dto.getSueldo());
    }
}
