package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.AgendaDTO;
import com.upc.cedorehab.entities.Agenda;
import com.upc.cedorehab.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    private AgendaDTO toAgendaDTO(Agenda a) {
        AgendaDTO dto = new AgendaDTO();
        dto.setAgendaId(a.getAgendaId());
        dto.setAnio(a.getAnio());
        dto.setMeses(a.getMeses());
        return dto;
    }

    @GetMapping("/agendas")
    public List<AgendaDTO> listarAgendas() {
        return agendaService.listar().stream().map(this::toAgendaDTO).collect(Collectors.toList());
    }

    @GetMapping("/agenda/{anio}")
    public ResponseEntity<AgendaDTO> buscarPorAnio(@PathVariable int anio) {
        Agenda a = agendaService.buscarPorAnio(anio);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toAgendaDTO(a));
    }

    @GetMapping("/agenda/id/{id}")
    public ResponseEntity<AgendaDTO> buscarPorId(@PathVariable long id) {
        Agenda a = agendaService.buscarPorId(id);
        if (a == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toAgendaDTO(a));
    }

    @PostMapping("/agenda")
    public ResponseEntity<AgendaDTO> adicionarAgenda(@RequestBody AgendaDTO dto) {
        Agenda existente = agendaService.buscarPorAnio(dto.getAnio());
        Agenda agenda;
        if (existente != null) {
            existente.setMeses(dto.getMeses());
            agenda = agendaService.editar(existente);
        } else {
            Agenda nuevo = new Agenda();
            nuevo.setAnio(dto.getAnio());
            nuevo.setMeses(dto.getMeses());
            agenda = agendaService.insertar(nuevo);
        }
        return ResponseEntity.ok(toAgendaDTO(agenda));
    }

    @PutMapping("/agenda")
    public ResponseEntity<AgendaDTO> editarAgenda(@RequestBody AgendaDTO dto) {
        Agenda existente = agendaService.buscarPorId(dto.getAgendaId());
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setAnio(dto.getAnio());
        existente.setMeses(dto.getMeses());
        existente = agendaService.editar(existente);
        return ResponseEntity.ok(toAgendaDTO(existente));
    }

    @DeleteMapping("/agenda/{id}")
    public ResponseEntity<Void> eliminarAgenda(@PathVariable long id) {
        agendaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}