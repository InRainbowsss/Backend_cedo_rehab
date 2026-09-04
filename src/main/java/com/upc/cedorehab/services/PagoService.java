package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.Pago;

import java.time.LocalDate;
import java.util.List;

public interface PagoService {
    Pago insertar(Pago pago);
    Pago editar(Pago pago);
    void eliminar(long id);
    List<Pago> listar();
    Pago buscarPorId(long id);
    List<Pago> listarPorRango(LocalDate inicio, LocalDate fin);
    List<Pago> listarPorFecha(LocalDate fecha);
}
