package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    Optional<Agenda> findByAnio(Integer anio);
}
