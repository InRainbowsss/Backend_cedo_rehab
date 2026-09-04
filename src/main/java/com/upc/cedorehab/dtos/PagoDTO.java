package com.upc.cedorehab.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Long pagoId;
    private LocalDate fecha;
    private BigDecimal monto;
    private String metodo;
    private String detalleMetodo;
    private Long paqueteId;
}
