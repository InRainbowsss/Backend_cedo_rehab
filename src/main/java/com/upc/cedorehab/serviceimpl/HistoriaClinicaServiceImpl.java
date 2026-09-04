package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.HistoriaClinica;
import com.upc.cedorehab.repositories.HistoriaClinicaRepository;
import com.upc.cedorehab.services.HistoriaClinicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoriaClinicaServiceImpl implements HistoriaClinicaService {
    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Override
    public HistoriaClinica insertar(HistoriaClinica historiaClinica) {
        return historiaClinicaRepository.save(historiaClinica);
    }

    @Override
    public HistoriaClinica editar(HistoriaClinica historiaClinica) {
        if (historiaClinicaRepository.findById(historiaClinica.getHistoriaId()).isPresent()) {
            return historiaClinicaRepository.save(historiaClinica);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (historiaClinicaRepository.existsById(id)) {
            historiaClinicaRepository.deleteById(id);
        }
    }

    @Override
    public List<HistoriaClinica> listar() {
        return historiaClinicaRepository.findAll();
    }

    @Override
    public HistoriaClinica buscarPorId(long id) {
        return historiaClinicaRepository.findById(id).orElse(null);
    }

    @Override
    public List<HistoriaClinica> listarPorPaciente(long pacienteId) {
        return historiaClinicaRepository.findByPacientePacienteId(pacienteId);
    }
}
