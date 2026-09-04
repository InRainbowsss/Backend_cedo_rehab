package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.ReencuentroInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReencuentroInventarioRepository extends JpaRepository<ReencuentroInventario, Long> {
    List<ReencuentroInventario> findByAnioAndMes(Integer anio, Integer mes);
    Optional<ReencuentroInventario> findByItemItemIdAndAnioAndMesAndNumeroSabado(Long itemId, Integer anio, Integer mes, Integer numeroSabado);
}
