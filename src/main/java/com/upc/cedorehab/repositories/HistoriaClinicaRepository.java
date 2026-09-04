package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {
    List<HistoriaClinica> findByPacientePacienteId(Long pacienteId);
}
