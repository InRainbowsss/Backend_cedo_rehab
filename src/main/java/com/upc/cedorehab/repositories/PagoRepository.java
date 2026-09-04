package com.upc.cedorehab.repositories;

import com.upc.cedorehab.entities.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByFechaBetween(LocalDate inicio, LocalDate fin);
    List<Pago> findByFecha(LocalDate fecha);
}
