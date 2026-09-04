package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.Paciente;

import java.util.List;

public interface PacienteService {
    Paciente insertar(Paciente paciente);
    Paciente editar(Paciente paciente);
    void eliminar(long id);
    List<Paciente> listar();
    Paciente buscarPorId(long id);
    List<Paciente> buscarPorNombre(String nombre);
    List<Paciente> buscarPorDni(String dni);
}
