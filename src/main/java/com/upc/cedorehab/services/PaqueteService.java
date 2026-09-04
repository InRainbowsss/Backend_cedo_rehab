package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.Paquete;

import java.util.List;

public interface PaqueteService {
    Paquete insertar(Paquete paquete);
    Paquete editar(Paquete paquete);
    void eliminar(long id);
    List<Paquete> listar();
    Paquete buscarPorId(long id);
    List<Paquete> listarPorPaciente(long pacienteId);
}
