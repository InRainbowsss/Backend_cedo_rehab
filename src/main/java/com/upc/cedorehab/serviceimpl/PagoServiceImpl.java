package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.Pago;
import com.upc.cedorehab.repositories.PagoRepository;
import com.upc.cedorehab.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagoServiceImpl implements PagoService {
    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public Pago insertar(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public Pago editar(Pago pago) {
        if (pagoRepository.findById(pago.getPagoId()).isPresent()) {
            return pagoRepository.save(pago);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (pagoRepository.existsById(id)) {
            pagoRepository.deleteById(id);
        }
    }

    @Override
    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    @Override
    public Pago buscarPorId(long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Pago> listarPorRango(LocalDate inicio, LocalDate fin) {
        return pagoRepository.findByFechaBetween(inicio, fin);
    }

    @Override
    public List<Pago> listarPorFecha(LocalDate fecha) {
        return pagoRepository.findByFecha(fecha);
    }
}
