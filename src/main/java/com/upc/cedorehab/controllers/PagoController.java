package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.PagoDTO;
import com.upc.cedorehab.entities.Pago;
import com.upc.cedorehab.entities.Paquete;
import com.upc.cedorehab.services.PagoService;
import com.upc.cedorehab.services.PaqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PaqueteService paqueteService;

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

    @GetMapping("/pagos")
    public List<PagoDTO> listarPagos(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String inicio,
            @RequestParam(required = false) String fin) {
        List<Pago> pagos;
        if (fecha != null && !fecha.isBlank()) {
            pagos = pagoService.listarPorFecha(LocalDate.parse(fecha));
        } else if (inicio != null && fin != null && !inicio.isBlank() && !fin.isBlank()) {
            pagos = pagoService.listarPorRango(LocalDate.parse(inicio), LocalDate.parse(fin));
        } else {
            pagos = pagoService.listar();
        }
        return pagos.stream().map(this::toPagoDTO).collect(Collectors.toList());
    }

    @PostMapping("/paquete/{paqueteId}/pago")
    public ResponseEntity<PagoDTO> adicionarPago(@PathVariable long paqueteId, @RequestBody PagoDTO dto) {
        Paquete paquete = paqueteService.buscarPorId(paqueteId);
        if (paquete == null) {
            return ResponseEntity.notFound().build();
        }
        Pago pago = new Pago();
        pago.setFecha(dto.getFecha());
        pago.setMonto(dto.getMonto());
        pago.setMetodo(dto.getMetodo());
        pago.setDetalleMetodo(dto.getDetalleMetodo());
        pago.setPaquete(paquete);
        pago = pagoService.insertar(pago);
        return ResponseEntity.ok(toPagoDTO(pago));
    }

    @PutMapping("/pago")
    public ResponseEntity<PagoDTO> editarPago(@RequestBody PagoDTO dto) {
        Pago existente = pagoService.buscarPorId(dto.getPagoId());
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setFecha(dto.getFecha());
        existente.setMonto(dto.getMonto());
        existente.setMetodo(dto.getMetodo());
        existente.setDetalleMetodo(dto.getDetalleMetodo());
        existente = pagoService.editar(existente);
        return ResponseEntity.ok(toPagoDTO(existente));
    }

    @DeleteMapping("/pago/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/finanzas/caja/{anio}/{mes}")
    public ResponseEntity<Map<String, Object>> obtenerCaja(@PathVariable int anio, @PathVariable int mes) {
        int mesNum = mes;
        LocalDate inicio = LocalDate.of(anio, mesNum, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        List<Pago> pagosMes = pagoService.listarPorRango(inicio, fin);
        BigDecimal totalMes = pagosMes.stream()
                .map(Pago::getMonto)
                .filter(m -> m != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate hoy = LocalDate.now();
        BigDecimal totalHoy = pagosMes.stream()
                .filter(p -> p.getFecha() != null && p.getFecha().equals(hoy))
                .map(Pago::getMonto)
                .filter(m -> m != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> caja = new HashMap<>();
        caja.put("anio", anio);
        caja.put("mes", mesNum);
        caja.put("totalMes", totalMes);
        caja.put("totalHoy", totalHoy);
        caja.put("cantidadPagos", pagosMes.size());
        return ResponseEntity.ok(caja);
    }
}
