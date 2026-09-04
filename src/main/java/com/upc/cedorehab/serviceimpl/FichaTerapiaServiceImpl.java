package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.FichaTerapia;
import com.upc.cedorehab.repositories.FichaTerapiaRepository;
import com.upc.cedorehab.services.FichaTerapiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FichaTerapiaServiceImpl implements FichaTerapiaService {
    @Autowired
    private FichaTerapiaRepository fichaTerapiaRepository;

    @Override
    public FichaTerapia insertar(FichaTerapia fichaTerapia) {
        return fichaTerapiaRepository.save(fichaTerapia);
    }

    @Override
    public FichaTerapia editar(FichaTerapia fichaTerapia) {
        if (fichaTerapiaRepository.findById(fichaTerapia.getFichaId()).isPresent()) {
            return fichaTerapiaRepository.save(fichaTerapia);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (fichaTerapiaRepository.existsById(id)) {
            fichaTerapiaRepository.deleteById(id);
        }
    }

    @Override
    public List<FichaTerapia> listar() {
        return fichaTerapiaRepository.findAll();
    }

    @Override
    public FichaTerapia buscarPorId(long id) {
        return fichaTerapiaRepository.findById(id).orElse(null);
    }

    @Override
    public List<FichaTerapia> listarPorPaciente(long pacienteId) {
        return fichaTerapiaRepository.findByPacientePacienteId(pacienteId);
    }
}
