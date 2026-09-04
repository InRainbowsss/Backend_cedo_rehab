package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.FichaTerapia;

import java.util.List;

public interface FichaTerapiaService {
    FichaTerapia insertar(FichaTerapia fichaTerapia);
    FichaTerapia editar(FichaTerapia fichaTerapia);
    void eliminar(long id);
    List<FichaTerapia> listar();
    FichaTerapia buscarPorId(long id);
    List<FichaTerapia> listarPorPaciente(long pacienteId);
}
