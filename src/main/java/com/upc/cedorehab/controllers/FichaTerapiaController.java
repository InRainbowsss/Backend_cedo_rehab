package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.FichaTerapiaDTO;
import com.upc.cedorehab.entities.FichaTerapia;
import com.upc.cedorehab.entities.Paciente;
import com.upc.cedorehab.services.FichaTerapiaService;
import com.upc.cedorehab.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class FichaTerapiaController {

    @Autowired
    private FichaTerapiaService fichaTerapiaService;

    @Autowired
    private PacienteService pacienteService;

    private FichaTerapiaDTO toFichaTerapiaDTO(FichaTerapia f) {
        FichaTerapiaDTO dto = new FichaTerapiaDTO();
        dto.setFichaId(f.getFichaId());
        dto.setNumeroFicha(f.getNumeroFicha());
        dto.setPaqueteNum(f.getPaqueteNum());
        dto.setTitulo(f.getTitulo());
        dto.setFecha(f.getFecha());
        dto.setFechaDx(f.getFechaDx());
        dto.setDx(f.getDx());
        dto.setTipoAtencion(f.getTipoAtencion());
        dto.setTerapias(f.getTerapias());
        dto.setIndicacionesExtra(f.getIndicacionesExtra());
        dto.setPacienteId(f.getPaciente() != null ? f.getPaciente().getPacienteId() : null);
        return dto;
    }

    @GetMapping("/fichas-terapias")
    public List<FichaTerapiaDTO> listarFichas() {
        return fichaTerapiaService.listar().stream().map(this::toFichaTerapiaDTO).collect(Collectors.toList());
    }

    @GetMapping("/paciente/{id}/fichas-terapias")
    public List<FichaTerapiaDTO> listarFichasPorPaciente(@PathVariable long id) {
        return fichaTerapiaService.listarPorPaciente(id).stream().map(this::toFichaTerapiaDTO).collect(Collectors.toList());
    }

    @GetMapping("/ficha-terapia/{id}")
    public ResponseEntity<FichaTerapiaDTO> buscarFicha(@PathVariable long id) {
        FichaTerapia f = fichaTerapiaService.buscarPorId(id);
        if (f == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toFichaTerapiaDTO(f));
    }

    @PostMapping("/paciente/{id}/ficha-terapia")
    public ResponseEntity<FichaTerapiaDTO> adicionarFicha(@PathVariable long id, @RequestBody FichaTerapiaDTO dto) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }
        FichaTerapia ficha = new FichaTerapia();
        ficha.setNumeroFicha(dto.getNumeroFicha());
        ficha.setPaqueteNum(dto.getPaqueteNum());
        ficha.setTitulo(dto.getTitulo());
        ficha.setFecha(dto.getFecha());
        ficha.setFechaDx(dto.getFechaDx());
        ficha.setDx(dto.getDx());
        ficha.setTipoAtencion(dto.getTipoAtencion());
        ficha.setTerapias(dto.getTerapias());
        ficha.setIndicacionesExtra(dto.getIndicacionesExtra());
        ficha.setPaciente(paciente);
        ficha = fichaTerapiaService.insertar(ficha);
        return ResponseEntity.ok(toFichaTerapiaDTO(ficha));
    }

    @PutMapping("/ficha-terapia")
    public ResponseEntity<FichaTerapiaDTO> editarFicha(@RequestBody FichaTerapiaDTO dto) {
        FichaTerapia existente = fichaTerapiaService.buscarPorId(dto.getFichaId());
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setNumeroFicha(dto.getNumeroFicha());
        existente.setPaqueteNum(dto.getPaqueteNum());
        existente.setTitulo(dto.getTitulo());
        existente.setFecha(dto.getFecha());
        existente.setFechaDx(dto.getFechaDx());
        existente.setDx(dto.getDx());
        existente.setTipoAtencion(dto.getTipoAtencion());
        existente.setTerapias(dto.getTerapias());
        existente.setIndicacionesExtra(dto.getIndicacionesExtra());
        existente = fichaTerapiaService.editar(existente);
        return ResponseEntity.ok(toFichaTerapiaDTO(existente));
    }

    @DeleteMapping("/ficha-terapia/{id}")
    public ResponseEntity<Void> eliminarFicha(@PathVariable long id) {
        fichaTerapiaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
