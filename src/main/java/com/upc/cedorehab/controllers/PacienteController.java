package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.FichaTerapiaDTO;
import com.upc.cedorehab.dtos.HistoriaClinicaDTO;
import com.upc.cedorehab.dtos.PacienteDTO;
import com.upc.cedorehab.dtos.PagoDTO;
import com.upc.cedorehab.dtos.PaqueteDTO;
import com.upc.cedorehab.entities.FichaTerapia;
import com.upc.cedorehab.entities.HistoriaClinica;
import com.upc.cedorehab.entities.Paciente;
import com.upc.cedorehab.entities.Pago;
import com.upc.cedorehab.entities.Paquete;
import com.upc.cedorehab.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    private LocalDate parseLocalDate(Object val) {
        if (val == null) return null;
        if (val instanceof LocalDate) return (LocalDate) val;
        String s = val.toString().trim();
        if (s.isEmpty()) return null;
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(Object val) {
        if (val == null) return null;
        if (val instanceof BigDecimal) return (BigDecimal) val;
        String s = val.toString().trim();
        if (s.isEmpty()) return null;
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInteger(Object val) {
        if (val == null) return null;
        if (val instanceof Integer) return (Integer) val;
        if (val instanceof Number) return ((Number) val).intValue();
        String s = val.toString().trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }

    private PacienteDTO toPacienteDTO(Paciente p) {
        PacienteDTO dto = new PacienteDTO();
        dto.setPacienteId(p.getPacienteId());
        dto.setId(p.getPacienteId());
        dto.setNombre(p.getNombre());
        dto.setEdad(p.getEdad());
        dto.setDni(p.getDni());
        dto.setCelular(p.getCelular());
        dto.setFechaIngreso(p.getFechaIngreso());
        dto.setColor(p.getColor());
        dto.setDniApoderado(p.getDniApoderado());
        dto.setConsultaMedica(p.getConsultaMedica());
        dto.setCostoConsulta(p.getCostoConsulta());
        dto.setCostoTerapia(p.getCostoTerapia());
        dto.setTipoAtencion(p.getTipoAtencion());
        dto.setPaqueteActual(p.getPaqueteActual());
        dto.setReevaluacionRequerida(p.getReevaluacionRequerida());
        dto.setCamposHC(p.getCamposHC() != null ? p.getCamposHC() : new ArrayList<>());
        dto.setBoletasPaquetes(p.getBoletasPaquetes() != null ? p.getBoletasPaquetes() : new HashMap<>());
        dto.setCamposHistoria(p.getCamposHistoria());

        if (p.getPaquetes() != null) {
            dto.setPaquetes(p.getPaquetes().stream().map(this::toPaqueteDTO).collect(Collectors.toList()));
            Map<String, Object> mapAsistencia = new HashMap<>();
            for (Paquete paq : p.getPaquetes()) {
                Map<String, Object> pData = new HashMap<>();
                pData.put("fechaInicio", paq.getFechaInicio() != null ? paq.getFechaInicio().toString() : "");
                pData.put("fechaTermino", paq.getFechaTermino() != null ? paq.getFechaTermino().toString() : "");
                pData.put("costo", paq.getCosto() != null ? paq.getCosto().toString() : "400");
                pData.put("sesiones", paq.getSesiones() != null ? paq.getSesiones() : 0);
                pData.put("boletasTerapias", paq.getBoletasTerapias() != null ? paq.getBoletasTerapias() : new HashMap<>());

                List<Map<String, Object>> pagosList = new ArrayList<>();
                if (paq.getPagos() != null) {
                    for (Pago pg : paq.getPagos()) {
                        Map<String, Object> pgData = new HashMap<>();
                        pgData.put("fecha", pg.getFecha() != null ? pg.getFecha().toString() : "");
                        pgData.put("monto", pg.getMonto() != null ? pg.getMonto().toString() : "0");
                        pgData.put("metodo", pg.getMetodo() != null ? pg.getMetodo() : "Efectivo");
                        pgData.put("detalleMetodo", pg.getDetalleMetodo() != null ? pg.getDetalleMetodo() : "");
                        pagosList.add(pgData);
                    }
                }
                pData.put("pagos", pagosList);
                mapAsistencia.put(String.valueOf(paq.getNumeroPaquete() != null ? paq.getNumeroPaquete() : 1), pData);
            }
            dto.setPaquetesAsistencia(mapAsistencia);
        }

        if (p.getFichasTerapias() != null) {
            dto.setFichasTerapias(p.getFichasTerapias().stream().map(this::toFichaTerapiaDTO).collect(Collectors.toList()));
        }

        if (p.getHistoriasClinicas() != null) {
            dto.setHistoriasClinicas(p.getHistoriasClinicas().stream().map(this::toHistoriaClinicaDTO).collect(Collectors.toList()));
            Map<String, List<Map<String, Object>>> mapHC = new HashMap<>();
            for (HistoriaClinica hc : p.getHistoriasClinicas()) {
                String paqKey = String.valueOf(hc.getNumeroPaquete() != null ? hc.getNumeroPaquete() : 1);
                mapHC.putIfAbsent(paqKey, new ArrayList<>());
                Map<String, Object> hcItem = new HashMap<>();
                hcItem.put("tipo", hc.getTipo());
                hcItem.put("fecha", hc.getFecha() != null ? hc.getFecha().toString() : "");
                hcItem.put("datos", hc.getDatos() != null ? hc.getDatos() : new HashMap<>());
                mapHC.get(paqKey).add(hcItem);
            }
            dto.setHistorialClinicoPaquetes(mapHC);
        }
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

    private FichaTerapiaDTO toFichaTerapiaDTO(FichaTerapia f) {
        FichaTerapiaDTO dto = new FichaTerapiaDTO();
        dto.setFichaId(f.getFichaId());
        dto.setIdFicha(f.getFichaId());
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

    @SuppressWarnings("unchecked")
    private void syncChildEntities(Paciente paciente, PacienteDTO dto) {
        // 1. Fichas de terapias
        if (dto.getFichasTerapias() != null) {
            paciente.getFichasTerapias().clear();
            for (FichaTerapiaDTO fDto : dto.getFichasTerapias()) {
                FichaTerapia f = new FichaTerapia();
                f.setNumeroFicha(fDto.getNumeroFicha() != null ? fDto.getNumeroFicha() : fDto.getPaqueteNum());
                f.setPaqueteNum(fDto.getPaqueteNum());
                f.setTitulo(fDto.getTitulo());
                f.setFecha(fDto.getFecha());
                f.setFechaDx(fDto.getFechaDx());
                f.setDx(fDto.getDx());
                f.setTipoAtencion(fDto.getTipoAtencion());
                f.setTerapias(fDto.getTerapias() != null ? fDto.getTerapias() : new ArrayList<>());
                f.setIndicacionesExtra(fDto.getIndicacionesExtra() != null ? fDto.getIndicacionesExtra() : new ArrayList<>());
                f.setPaciente(paciente);
                paciente.getFichasTerapias().add(f);
            }
        }

        // 2. Historial clinico por paquetes
        if (dto.getHistorialClinicoPaquetes() != null) {
            paciente.getHistoriasClinicas().clear();
            for (Map.Entry<String, List<Map<String, Object>>> entry : dto.getHistorialClinicoPaquetes().entrySet()) {
                Integer numPaquete = parseInteger(entry.getKey());
                if (numPaquete == null) numPaquete = 1;
                List<Map<String, Object>> items = entry.getValue();
                if (items != null) {
                    for (Map<String, Object> item : items) {
                        HistoriaClinica h = new HistoriaClinica();
                        h.setNumeroPaquete(numPaquete);
                        h.setTipo(item.get("tipo") != null ? item.get("tipo").toString() : "PRE-CONSULTA");
                        h.setFecha(parseLocalDate(item.get("fecha")));
                        h.setDatos(item.get("datos") instanceof Map ? (Map<String, Object>) item.get("datos") : new HashMap<>());
                        h.setPaciente(paciente);
                        paciente.getHistoriasClinicas().add(h);
                    }
                }
            }
        }

        // 3. Paquetes de asistencia y pagos
        if (dto.getPaquetesAsistencia() != null) {
            paciente.getPaquetes().clear();
            for (Map.Entry<String, Object> entry : dto.getPaquetesAsistencia().entrySet()) {
                Integer numPaquete = parseInteger(entry.getKey());
                if (numPaquete == null) numPaquete = 1;
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> paqMap = (Map<String, Object>) entry.getValue();
                    Paquete paq = new Paquete();
                    paq.setNumeroPaquete(numPaquete);
                    paq.setFechaInicio(parseLocalDate(paqMap.get("fechaInicio")));
                    paq.setFechaTermino(parseLocalDate(paqMap.get("fechaTermino")));
                    paq.setCosto(parseBigDecimal(paqMap.get("costo")));
                    paq.setSesiones(parseInteger(paqMap.get("sesiones")));
                    if (paqMap.get("boletasTerapias") instanceof Map) {
                        Map<String, Boolean> boletas = new HashMap<>();
                        Map<?, ?> rawBoletas = (Map<?, ?>) paqMap.get("boletasTerapias");
                        for (Map.Entry<?, ?> bEntry : rawBoletas.entrySet()) {
                            boletas.put(bEntry.getKey().toString(), Boolean.valueOf(bEntry.getValue().toString()));
                        }
                        paq.setBoletasTerapias(boletas);
                    }
                    paq.setPaciente(paciente);

                    if (paqMap.get("pagos") instanceof List) {
                        List<?> pagosList = (List<?>) paqMap.get("pagos");
                        for (Object pObj : pagosList) {
                            if (pObj instanceof Map) {
                                Map<?, ?> pMap = (Map<?, ?>) pObj;
                                Pago pago = new Pago();
                                pago.setFecha(parseLocalDate(pMap.get("fecha")));
                                pago.setMonto(parseBigDecimal(pMap.get("monto")));
                                pago.setMetodo(pMap.get("metodo") != null ? pMap.get("metodo").toString() : "Efectivo");
                                pago.setDetalleMetodo(pMap.get("detalleMetodo") != null ? pMap.get("detalleMetodo").toString() : "");
                                pago.setPaquete(paq);
                                paq.getPagos().add(pago);
                            }
                        }
                    }
                    paciente.getPaquetes().add(paq);
                }
            }
        }
    }

    @GetMapping("/pacientes")
    public List<PacienteDTO> listarPacientes() {
        return pacienteService.listar().stream().map(this::toPacienteDTO).collect(Collectors.toList());
    }

    @GetMapping("/pacientes/buscar")
    public List<PacienteDTO> buscarPacientes(@RequestParam(required = false) String nombre,
                                              @RequestParam(required = false) String dni) {
        if (nombre != null && !nombre.isBlank()) {
            return pacienteService.buscarPorNombre(nombre).stream().map(this::toPacienteDTO).collect(Collectors.toList());
        }
        if (dni != null && !dni.isBlank()) {
            return pacienteService.buscarPorDni(dni).stream().map(this::toPacienteDTO).collect(Collectors.toList());
        }
        return listarPacientes();
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<PacienteDTO> buscarPaciente(@PathVariable long id) {
        Paciente p = pacienteService.buscarPorId(id);
        if (p == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toPacienteDTO(p));
    }

    @PostMapping("/paciente")
    public ResponseEntity<PacienteDTO> adicionarPaciente(@RequestBody PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setEdad(dto.getEdad());
        paciente.setDni(dto.getDni());
        paciente.setCelular(dto.getCelular());
        paciente.setFechaIngreso(dto.getFechaIngreso() != null ? dto.getFechaIngreso() : LocalDate.now());
        paciente.setColor(dto.getColor() == null ? "color-verde" : dto.getColor());
        paciente.setDniApoderado(dto.getDniApoderado());
        paciente.setConsultaMedica(dto.getConsultaMedica() != null ? dto.getConsultaMedica() : "no");
        paciente.setCostoConsulta(dto.getCostoConsulta() != null ? dto.getCostoConsulta() : BigDecimal.ZERO);
        paciente.setCostoTerapia(dto.getCostoTerapia() != null ? dto.getCostoTerapia() : new BigDecimal("40"));
        paciente.setTipoAtencion(dto.getTipoAtencion() == null ? "PARTICULAR" : dto.getTipoAtencion());
        paciente.setPaqueteActual(dto.getPaqueteActual() == null ? 1 : dto.getPaqueteActual());
        paciente.setReevaluacionRequerida(dto.getReevaluacionRequerida() != null ? dto.getReevaluacionRequerida() : false);
        paciente.setCamposHC(dto.getCamposHC() != null ? dto.getCamposHC() : List.of("Nombre", "Edad", "DNI", "Celular", "Síntomas", "Examen Físico"));
        paciente.setBoletasPaquetes(dto.getBoletasPaquetes() != null ? dto.getBoletasPaquetes() : new HashMap<>());
        paciente.setCamposHistoria(dto.getCamposHistoria());

        syncChildEntities(paciente, dto);

        paciente = pacienteService.insertar(paciente);
        return ResponseEntity.ok(toPacienteDTO(paciente));
    }

    @PutMapping("/paciente")
    public ResponseEntity<PacienteDTO> editarPaciente(@RequestBody PacienteDTO dto) {
        Long id = dto.getPacienteId() != null ? dto.getPacienteId() : dto.getId();
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Paciente existente = pacienteService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        if (dto.getNombre() != null) existente.setNombre(dto.getNombre());
        if (dto.getEdad() != null) existente.setEdad(dto.getEdad());
        if (dto.getDni() != null) existente.setDni(dto.getDni());
        if (dto.getCelular() != null) existente.setCelular(dto.getCelular());
        if (dto.getFechaIngreso() != null) existente.setFechaIngreso(dto.getFechaIngreso());
        if (dto.getColor() != null) existente.setColor(dto.getColor());
        if (dto.getDniApoderado() != null) existente.setDniApoderado(dto.getDniApoderado());
        if (dto.getConsultaMedica() != null) existente.setConsultaMedica(dto.getConsultaMedica());
        if (dto.getCostoConsulta() != null) existente.setCostoConsulta(dto.getCostoConsulta());
        if (dto.getCostoTerapia() != null) existente.setCostoTerapia(dto.getCostoTerapia());
        if (dto.getTipoAtencion() != null) existente.setTipoAtencion(dto.getTipoAtencion());
        if (dto.getPaqueteActual() != null) existente.setPaqueteActual(dto.getPaqueteActual());
        if (dto.getReevaluacionRequerida() != null) existente.setReevaluacionRequerida(dto.getReevaluacionRequerida());
        if (dto.getCamposHC() != null) existente.setCamposHC(dto.getCamposHC());
        if (dto.getBoletasPaquetes() != null) existente.setBoletasPaquetes(dto.getBoletasPaquetes());
        if (dto.getCamposHistoria() != null) existente.setCamposHistoria(dto.getCamposHistoria());

        syncChildEntities(existente, dto);

        existente = pacienteService.editar(existente);
        return ResponseEntity.ok(toPacienteDTO(existente));
    }

    @PutMapping("/paciente/{id}")
    public ResponseEntity<PacienteDTO> actualizarPacientePorId(@PathVariable long id, @RequestBody PacienteDTO dto) {
        dto.setId(id);
        dto.setPacienteId(id);
        return editarPaciente(dto);
    }

    @DeleteMapping("/paciente/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
