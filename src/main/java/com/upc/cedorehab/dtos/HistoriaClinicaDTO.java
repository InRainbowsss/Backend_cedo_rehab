package com.upc.cedorehab.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriaClinicaDTO {
    private Long historiaId;
    private Integer numeroPaquete;
    private String tipo;
    private LocalDate fecha;
    private Map<String, Object> datos;
    private Long pacienteId;
}
