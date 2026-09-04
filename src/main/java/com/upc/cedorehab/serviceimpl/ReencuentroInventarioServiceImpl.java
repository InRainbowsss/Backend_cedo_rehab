package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.ReencuentroInventario;
import com.upc.cedorehab.repositories.ReencuentroInventarioRepository;
import com.upc.cedorehab.services.ReencuentroInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReencuentroInventarioServiceImpl implements ReencuentroInventarioService {
    @Autowired
    private ReencuentroInventarioRepository reencuentroInventarioRepository;

    @Override
    public ReencuentroInventario insertar(ReencuentroInventario reencuentro) {
        return reencuentroInventarioRepository.save(reencuentro);
    }

    @Override
    public ReencuentroInventario editar(ReencuentroInventario reencuentro) {
        if (reencuentroInventarioRepository.findById(reencuentro.getReencuentroId()).isPresent()) {
            return reencuentroInventarioRepository.save(reencuentro);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (reencuentroInventarioRepository.existsById(id)) {
            reencuentroInventarioRepository.deleteById(id);
        }
    }

    @Override
    public List<ReencuentroInventario> listar() {
        return reencuentroInventarioRepository.findAll();
    }

    @Override
    public ReencuentroInventario buscarPorId(long id) {
        return reencuentroInventarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<ReencuentroInventario> listarPorMesAnio(int anio, int mes) {
        return reencuentroInventarioRepository.findByAnioAndMes(anio, mes);
    }

    @Override
    public ReencuentroInventario buscarPorItemMesSabado(long itemId, int anio, int mes, int numeroSabado) {
        return reencuentroInventarioRepository.findByItemItemIdAndAnioAndMesAndNumeroSabado(itemId, anio, mes, numeroSabado).orElse(null);
    }
}
