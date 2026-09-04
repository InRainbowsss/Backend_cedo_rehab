package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.ReencuentroInventarioDTO;
import com.upc.cedorehab.entities.InventarioItem;
import com.upc.cedorehab.entities.ReencuentroInventario;
import com.upc.cedorehab.services.InventarioItemService;
import com.upc.cedorehab.services.ReencuentroInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class ReencuentroInventarioController {

    @Autowired
    private ReencuentroInventarioService reencuentroInventarioService;

    @Autowired
    private InventarioItemService inventarioItemService;

    private ReencuentroInventarioDTO toReencuentroDTO(ReencuentroInventario r) {
        ReencuentroInventarioDTO dto = new ReencuentroInventarioDTO();
        dto.setReencuentroId(r.getReencuentroId());
        dto.setAnio(r.getAnio());
        dto.setMes(r.getMes());
        dto.setNumeroSabado(r.getNumeroSabado());
        dto.setCantidad(r.getCantidad());
        dto.setItemId(r.getItem() != null ? r.getItem().getItemId() : null);
        return dto;
    }

    @GetMapping("/reencuentro-inventario")
    public List<ReencuentroInventarioDTO> listar(
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer mes) {
        if (anio != null && mes != null) {
            return reencuentroInventarioService.listarPorMesAnio(anio, mes).stream()
                    .map(this::toReencuentroDTO).collect(Collectors.toList());
        }
        return reencuentroInventarioService.listar().stream().map(this::toReencuentroDTO).collect(Collectors.toList());
    }

    @GetMapping("/reencuentro-inventario/map")
    public Map<String, Integer> obtenerMapa() {
        List<ReencuentroInventario> list = reencuentroInventarioService.listar();
        Map<String, Integer> map = new HashMap<>();
        for (ReencuentroInventario r : list) {
            if (r.getItem() != null && r.getItem().getItemId() != null && r.getNumeroSabado() != null) {
                String key = r.getItem().getItemId() + "_s" + r.getNumeroSabado();
                map.put(key, r.getCantidad());
            }
        }
        return map;
    }

    @PostMapping("/reencuentro-inventario/actualizar")
    public ResponseEntity<?> actualizarSabado(@RequestBody Map<String, Object> body) {
        Long itemId = Long.valueOf(body.get("itemId").toString());
        String sabadoKey = body.get("sabadoKey").toString();
        int sabadoNum = Integer.parseInt(sabadoKey.replaceAll("[^0-9]", ""));
        Integer cantidad = Integer.valueOf(body.get("cantidad").toString());
        Integer anio = body.containsKey("anio") ? Integer.valueOf(body.get("anio").toString()) : 2026;
        Integer mes = body.containsKey("mes") ? Integer.valueOf(body.get("mes").toString()) : 8;

        InventarioItem item = inventarioItemService.buscarPorId(itemId);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        ReencuentroInventario existente = reencuentroInventarioService.buscarPorItemMesSabado(
                itemId, anio, mes, sabadoNum);
        if (existente != null) {
            existente.setCantidad(cantidad);
            reencuentroInventarioService.editar(existente);
        } else {
            ReencuentroInventario nuevo = new ReencuentroInventario();
            nuevo.setAnio(anio);
            nuevo.setMes(mes);
            nuevo.setNumeroSabado(sabadoNum);
            nuevo.setCantidad(cantidad);
            nuevo.setItem(item);
            reencuentroInventarioService.insertar(nuevo);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/reencuentro-inventario")
    public ResponseEntity<ReencuentroInventarioDTO> adicionar(@RequestBody ReencuentroInventarioDTO dto) {
        InventarioItem item = inventarioItemService.buscarPorId(dto.getItemId());
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        ReencuentroInventario existente = reencuentroInventarioService.buscarPorItemMesSabado(
                dto.getItemId(), dto.getAnio(), dto.getMes(), dto.getNumeroSabado());
        ReencuentroInventario reencuentro;
        if (existente != null) {
            existente.setCantidad(dto.getCantidad());
            reencuentro = reencuentroInventarioService.editar(existente);
        } else {
            ReencuentroInventario nuevo = new ReencuentroInventario();
            nuevo.setAnio(dto.getAnio());
            nuevo.setMes(dto.getMes());
            nuevo.setNumeroSabado(dto.getNumeroSabado());
            nuevo.setCantidad(dto.getCantidad());
            nuevo.setItem(item);
            reencuentro = reencuentroInventarioService.insertar(nuevo);
        }
        return ResponseEntity.ok(toReencuentroDTO(reencuentro));
    }

    @DeleteMapping("/reencuentro-inventario/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        reencuentroInventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}