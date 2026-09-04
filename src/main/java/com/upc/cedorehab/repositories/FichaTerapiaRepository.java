package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.FichaTerapia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FichaTerapiaRepository extends JpaRepository<FichaTerapia, Long> {
    List<FichaTerapia> findByPacientePacienteId(Long pacienteId);
}
