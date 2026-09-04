package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.PagoDTO;
import com.upc.cedorehab.dtos.PaqueteDTO;
import com.upc.cedorehab.entities.Pago;
import com.upc.cedorehab.entities.Paciente;
import com.upc.cedorehab.entities.Paquete;
import com.upc.cedorehab.services.PacienteService;
import com.upc.cedorehab.services.PaqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class PaqueteController {

    @Autowired
    private PaqueteService paqueteService;

    @Autowired
    private PacienteService pacienteService;

    private PagoDTO toPagoDTO(Pago pg) {
        PagoDTO dto = new PagoDTO();
        dto.setPagoId(pg.getPagoId());
        dto.setFecha(pg.getFecha());
        dto.setMonto(pg.getMonto());
        dto.setMetodo(pg.getMetodo());
        dto.setDetalleMetodo(pg.getDetalleMetodo());
        dto.setPaqueteId(pg.getPaquete() != null ? pg.getPaquete().getPaqueteId() : null);
        return dto;
    }

    private PaqueteDTO toPaqueteDTO(Paquete q) {
        PaqueteDTO dto = new PaqueteDTO();
        dto.setPaqueteId(q.getPaqueteId());
        dto.setNumeroPaquete(q.getNumeroPaquete());
        dto.setFechaInicio(q.getFechaInicio());
        dto.setFechaTermino(q.getFechaTermino());
        dto.setCosto(q.getCosto());
        dto.setSesiones(q.getSesiones());
        dto.setBoletasTerapias(q.getBoletasTerapias());
        dto.setPacienteId(q.getPaciente() != null ? q.getPaciente().getPacienteId() : null);
        if (q.getPagos() != null) {
            dto.setPagos(q.getPagos().stream().map(this::toPagoDTO).collect(Collectors.toList()));
        }
        return dto;
    }

    @GetMapping("/paquetes")
    public List<PaqueteDTO> listarPaquetes() {
        return paqueteService.listar().stream().map(this::toPaqueteDTO).collect(Collectors.toList());
    }

    @GetMapping("/paciente/{id}/paquetes")
    public List<PaqueteDTO> listarPaquetesPorPaciente(@PathVariable long id) {
        return paqueteService.listarPorPaciente(id).stream().map(this::toPaqueteDTO).collect(Collectors.toList());
    }

    @GetMapping("/paquete/{id}")
    public ResponseEntity<PaqueteDTO> buscarPaquete(@PathVariable long id) {
        Paquete q = paqueteService.buscarPorId(id);
        if (q == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toPaqueteDTO(q));
    }

    @PostMapping("/paciente/{id}/paquete")
    public ResponseEntity<PaqueteDTO> adicionarPaquete(@PathVariable long id, @RequestBody PaqueteDTO dto) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        Paquete paquete = new Paquete();
        paquete.setNumeroPaquete(dto.getNumeroPaquete());
        paquete.setFechaInicio(dto.getFechaInicio());
        paquete.setFechaTermino(dto.getFechaTermino());
        paquete.setCosto(dto.getCosto());
        paquete.setSesiones(dto.getSesiones() != null ? dto.getSesiones() : 0);
        paquete.setBoletasTerapias(dto.getBoletasTerapias());
        paquete.setPaciente(paciente);
        paquete = paqueteService.insertar(paquete);
        return ResponseEntity.ok(toPaqueteDTO(paquete));
    }

    @PutMapping("/paquete")
    public ResponseEntity<PaqueteDTO> editarPaquete(@RequestBody PaqueteDTO dto) {
        Paquete existente = paqueteService.buscarPorId(dto.getPaqueteId());
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setNumeroPaquete(dto.getNumeroPaquete());
        existente.setFechaInicio(dto.getFechaInicio());
        existente.setFechaTermino(dto.getFechaTermino());
        existente.setCosto(dto.getCosto());
        existente.setSesiones(dto.getSesiones());
        existente.setBoletasTerapias(dto.getBoletasTerapias());
        existente = paqueteService.editar(existente);
        return ResponseEntity.ok(toPaqueteDTO(existente));
    }

    @DeleteMapping("/paquete/{id}")
    public ResponseEntity<Void> eliminarPaquete(@PathVariable long id) {
        paqueteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
