package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.HistoriaClinicaDTO;
import com.upc.cedorehab.entities.HistoriaClinica;
import com.upc.cedorehab.entities.Paciente;
import com.upc.cedorehab.services.HistoriaClinicaService;
import com.upc.cedorehab.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class HistoriaClinicaController {

    @Autowired
    private HistoriaClinicaService historiaClinicaService;

    @Autowired
    private PacienteService pacienteService;

    private HistoriaClinicaDTO toHistoriaClinicaDTO(HistoriaClinica h) {
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        dto.setHistoriaId(h.getHistoriaId());
        dto.setNumeroPaquete(h.getNumeroPaquete());
        dto.setTipo(h.getTipo());
        dto.setFecha(h.getFecha());
        dto.setDatos(h.getDatos());
        dto.setPacienteId(h.getPaciente() != null ? h.getPaciente().getPacienteId() : null);
        return dto;
    }

    @GetMapping("/historias-clinicas")
    public List<HistoriaClinicaDTO> listarHistorias() {
        return historiaClinicaService.listar().stream().map(this::toHistoriaClinicaDTO).collect(Collectors.toList());
    }

    @GetMapping("/paciente/{id}/historias-clinicas")
    public List<HistoriaClinicaDTO> listarHistoriasPorPaciente(@PathVariable long id) {
        return historiaClinicaService.listarPorPaciente(id).stream().map(this::toHistoriaClinicaDTO).collect(Collectors.toList());
    }

    @GetMapping("/historia-clinica/{id}")
    public ResponseEntity<HistoriaClinicaDTO> buscarHistoria(@PathVariable long id) {
        HistoriaClinica h = historiaClinicaService.buscarPorId(id);
        if (h == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toHistoriaClinicaDTO(h));
    }

    @PostMapping("/paciente/{id}/historia-clinica")
    public ResponseEntity<HistoriaClinicaDTO> adicionarHistoria(@PathVariable long id, @RequestBody HistoriaClinicaDTO dto) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        HistoriaClinica historia = new HistoriaClinica();
        historia.setNumeroPaquete(dto.getNumeroPaquete());
        historia.setTipo(dto.getTipo());
        historia.setFecha(dto.getFecha());
        historia.setDatos(dto.getDatos());
        historia.setPaciente(paciente);
        historia = historiaClinicaService.insertar(historia);
        return ResponseEntity.ok(toHistoriaClinicaDTO(historia));
    }

    @PutMapping("/historia-clinica")
    public ResponseEntity<HistoriaClinicaDTO> editarHistoria(@RequestBody HistoriaClinicaDTO dto) {
        HistoriaClinica existente = historiaClinicaService.buscarPorId(dto.getHistoriaId());
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setNumeroPaquete(dto.getNumeroPaquete());
        existente.setTipo(dto.getTipo());
        existente.setFecha(dto.getFecha());
        existente.setDatos(dto.getDatos());
        existente = historiaClinicaService.editar(existente);
        return ResponseEntity.ok(toHistoriaClinicaDTO(existente));
    }

    @DeleteMapping("/historia-clinica/{id}")
    public ResponseEntity<Void> eliminarHistoria(@PathVariable long id) {
        historiaClinicaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
