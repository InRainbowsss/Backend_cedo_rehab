package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.HistoriaClinica;

import java.util.List;

public interface HistoriaClinicaService {
    HistoriaClinica insertar(HistoriaClinica historiaClinica);
    HistoriaClinica editar(HistoriaClinica historiaClinica);
    void eliminar(long id);
    List<HistoriaClinica> listar();
    HistoriaClinica buscarPorId(long id);
    List<HistoriaClinica> listarPorPaciente(long pacienteId);
}
