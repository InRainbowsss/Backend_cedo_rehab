package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.Paquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaqueteRepository extends JpaRepository<Paquete, Long> {
    List<Paquete> findByPacientePacienteId(Long pacienteId);
}
