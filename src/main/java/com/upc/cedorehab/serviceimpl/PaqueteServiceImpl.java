package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.Paquete;
import com.upc.cedorehab.repositories.PaqueteRepository;
import com.upc.cedorehab.services.PaqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaqueteServiceImpl implements PaqueteService {
    @Autowired
    private PaqueteRepository paqueteRepository;

    @Override
    public Paquete insertar(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    @Override
    public Paquete editar(Paquete paquete) {
        if (paqueteRepository.findById(paquete.getPaqueteId()).isPresent()) {
            return paqueteRepository.save(paquete);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (paqueteRepository.existsById(id)) {
            paqueteRepository.deleteById(id);
        }
    }

    @Override
    public List<Paquete> listar() {
        return paqueteRepository.findAll();
    }

    @Override
    public Paquete buscarPorId(long id) {
        return paqueteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Paquete> listarPorPaciente(long pacienteId) {
        return paqueteRepository.findByPacientePacienteId(pacienteId);
    }
}
