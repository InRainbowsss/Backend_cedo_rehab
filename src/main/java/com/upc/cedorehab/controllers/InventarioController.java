package com.upc.cedorehab.controllers;

import com.upc.cedorehab.dtos.InventarioItemDTO;
import com.upc.cedorehab.dtos.ReencuentroInventarioDTO;
import com.upc.cedorehab.entities.InventarioItem;
import com.upc.cedorehab.entities.ReencuentroInventario;
import com.upc.cedorehab.services.InventarioItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(originPatterns = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RequestMapping("/api")
public class InventarioController {

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

    private InventarioItemDTO toInventarioDTO(InventarioItem item) {
        InventarioItemDTO dto = new InventarioItemDTO();
        dto.setItemId(item.getItemId());
        dto.setNombre(item.getNombre());
        dto.setCantidad(item.getCantidad());
        dto.setImagen(item.getImagen());
        if (item.getReencuentros() != null) {
            dto.setReencuentros(item.getReencuentros().stream().map(this::toReencuentroDTO).collect(Collectors.toList()));
        }
        return dto;
    }

    @GetMapping("/inventario")
    public List<InventarioItemDTO> listarInventario() {
        return inventarioItemService.listar().stream().map(this::toInventarioDTO).collect(Collectors.toList());
    }

    @GetMapping("/inventario/{id}")
    public ResponseEntity<InventarioItemDTO> buscarItem(@PathVariable long id) {
        InventarioItem item = inventarioItemService.buscarPorId(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toInventarioDTO(item));
    }

    @PostMapping("/inventario")
    public ResponseEntity<InventarioItemDTO> adicionarItem(@RequestBody InventarioItemDTO dto) {
        InventarioItem item = new InventarioItem();
        item.setNombre(dto.getNombre());
        item.setCantidad(dto.getCantidad());
        item.setImagen(dto.getImagen());
        item = inventarioItemService.insertar(item);
        return ResponseEntity.ok(toInventarioDTO(item));
    }

    @PutMapping("/inventario")
    public ResponseEntity<InventarioItemDTO> editarItem(@RequestBody InventarioItemDTO dto) {
        Long id = dto.getItemId() != null ? dto.getItemId() : dto.getId();
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        InventarioItem existente = inventarioItemService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setNombre(dto.getNombre());
        existente.setCantidad(dto.getCantidad());
        existente.setImagen(dto.getImagen());
        existente = inventarioItemService.editar(existente);
        return ResponseEntity.ok(toInventarioDTO(existente));
    }

    @DeleteMapping("/inventario/{id}")
    public ResponseEntity<Void> eliminarItem(@PathVariable long id) {
        inventarioItemService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
