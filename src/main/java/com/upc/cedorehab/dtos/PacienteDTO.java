package com.upc.cedorehab.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {
    private Long pacienteId;

    @JsonProperty("id")
    public Long getId() {
        return pacienteId;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        if (id != null) {
            this.pacienteId = id;
        }
    }

    private String nombre;
    private String edad;
    private String dni;
    private String celular;
    private LocalDate fechaIngreso;
    private String color;
    private String dniApoderado;
    private String consultaMedica;
    private BigDecimal costoConsulta;
    private BigDecimal costoTerapia;
    private String tipoAtencion;
    private Integer paqueteActual;
    private Boolean reevaluacionRequerida;
    private List<String> camposHC = new ArrayList<>();
    private Map<String, Boolean> boletasPaquetes;
    private List<Map<String, Object>> camposHistoria;
    private List<PaqueteDTO> paquetes = new ArrayList<>();
    private List<FichaTerapiaDTO> fichasTerapias = new ArrayList<>();
    private List<HistoriaClinicaDTO> historiasClinicas = new ArrayList<>();

    // Mapas compatibles con el frontend
    private Map<String, List<Map<String, Object>>> historialClinicoPaquetes;
    private Map<String, Object> paquetesAsistencia;
}
