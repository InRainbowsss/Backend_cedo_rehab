package com.upc.cedorehab.services;

import com.upc.cedorehab.entities.InventarioItem;

import java.util.List;

public interface InventarioItemService {
    InventarioItem insertar(InventarioItem item);
    InventarioItem editar(InventarioItem item);
    void eliminar(long id);
    List<InventarioItem> listar();
    InventarioItem buscarPorId(long id);
}
