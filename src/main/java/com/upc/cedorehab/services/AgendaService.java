package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.Agenda;

import java.util.List;

public interface AgendaService {
    Agenda insertar(Agenda agenda);
    Agenda editar(Agenda agenda);
    void eliminar(long id);
    List<Agenda> listar();
    Agenda buscarPorId(long id);
    Agenda buscarPorAnio(int anio);
}
