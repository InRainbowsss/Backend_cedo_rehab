package com.upc.cedorehab.dtos;

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
public class PaqueteDTO {
    private Long paqueteId;
    private Integer numeroPaquete;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private BigDecimal costo;
    private Integer sesiones;
    private Map<String, Boolean> boletasTerapias;
    private Long pacienteId;
    private List<PagoDTO> pagos = new ArrayList<>();
}
