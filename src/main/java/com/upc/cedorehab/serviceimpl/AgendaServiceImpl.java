package com.upc.cedorehab.serviceimpl;

import com.upc.cedorehab.entities.Agenda;
import com.upc.cedorehab.repositories.AgendaRepository;
import com.upc.cedorehab.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaServiceImpl implements AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Override
    public Agenda insertar(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    @Override
    public Agenda editar(Agenda agenda) {
        if (agendaRepository.findById(agenda.getAgendaId()).isPresent()) {
            return agendaRepository.save(agenda);
        }
        return null;
    }

    @Override
    public void eliminar(long id) {
        if (agendaRepository.existsById(id)) {
            agendaRepository.deleteById(id);
        }
    }

    @Override
    public List<Agenda> listar() {
        return agendaRepository.findAll();
    }

    @Override
    public Agenda buscarPorId(long id) {
        return agendaRepository.findById(id).orElse(null);
    }

    @Override
    public Agenda buscarPorAnio(int anio) {
        return agendaRepository.findByAnio(anio).orElse(null);
    }
}
