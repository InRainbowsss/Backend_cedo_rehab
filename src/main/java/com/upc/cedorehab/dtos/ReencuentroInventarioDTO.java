package com.upc.cedorehab.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReencuentroInventarioDTO {
    private Long reencuentroId;
    private Integer anio;
    private Integer mes;
    private Integer numeroSabado;
    private Integer cantidad;
    private Long itemId;
}
