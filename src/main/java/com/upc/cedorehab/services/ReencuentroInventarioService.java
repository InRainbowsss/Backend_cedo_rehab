package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.ReencuentroInventario;

import java.util.List;

public interface ReencuentroInventarioService {
    ReencuentroInventario insertar(ReencuentroInventario reencuentro);
    ReencuentroInventario editar(ReencuentroInventario reencuentro);
    void eliminar(long id);
    List<ReencuentroInventario> listar();
    ReencuentroInventario buscarPorId(long id);
    List<ReencuentroInventario> listarPorMesAnio(int anio, int mes);
    ReencuentroInventario buscarPorItemMesSabado(long itemId, int anio, int mes, int numeroSabado);
}
