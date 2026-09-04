package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.Paciente;
import com.upc.cedorehab.repositories.PacienteRepository;
import com.upc.cedorehab.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Override
    public Paciente insertar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente editar(Paciente paciente) {
        if (pacienteRepository.findById(paciente.getPacienteId()).isPresent()) {
            return pacienteRepository.save(paciente);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
        }
    }

    @Override
    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente buscarPorId(long id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Paciente> buscarPorDni(String dni) {
        return pacienteRepository.findByDniContaining(dni);
    }
}
