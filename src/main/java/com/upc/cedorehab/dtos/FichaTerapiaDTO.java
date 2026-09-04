package com.upc.cedorehab.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FichaTerapiaDTO {
    private Long fichaId;

    @JsonProperty("idFicha")
    public Long getIdFicha() {
        return fichaId;
    }

    @JsonProperty("idFicha")
    public void setIdFicha(Long idFicha) {
        if (idFicha != null) {
            this.fichaId = idFicha;
        }
    }

    private Integer numeroFicha;
    private Integer paqueteNum;
    private String titulo;
    private LocalDate fecha;
    private LocalDate fechaDx;
    private String dx;
    private String tipoAtencion;
    private List<String> terapias = new ArrayList<>();
    private List<String> indicacionesExtra = new ArrayList<>();
    private Long pacienteId;
}
