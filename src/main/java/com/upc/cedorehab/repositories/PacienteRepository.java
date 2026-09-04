package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByDni(String dni);
    List<Paciente> findByNombreContainingIgnoreCase(String nombre);
    List<Paciente> findByDniContaining(String dni);
}
