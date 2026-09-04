package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.InventarioItem;
import com.upc.cedorehab.repositories.InventarioItemRepository;
import com.upc.cedorehab.services.InventarioItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioItemServiceImpl implements InventarioItemService {
    @Autowired
    private InventarioItemRepository inventarioItemRepository;

    @Override
    public InventarioItem insertar(InventarioItem item) {
        return inventarioItemRepository.save(item);
    }

    @Override
    public InventarioItem editar(InventarioItem item) {
        if (inventarioItemRepository.findById(item.getItemId()).isPresent()) {
            return inventarioItemRepository.save(item);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (inventarioItemRepository.existsById(id)) {
            inventarioItemRepository.deleteById(id);
        }
    }

    @Override
    public List<InventarioItem> listar() {
        return inventarioItemRepository.findAll();
    }

    @Override
    public InventarioItem buscarPorId(long id) {
        return inventarioItemRepository.findById(id).orElse(null);
    }
}
