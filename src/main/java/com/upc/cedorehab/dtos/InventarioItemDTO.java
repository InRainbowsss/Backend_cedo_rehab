package com.upc.cedorehab.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventarioItemDTO {
    private Long itemId;

    @JsonProperty("id")
    public Long getId() {
        return itemId;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        if (id != null) {
            this.itemId = id;
        }
    }

    private String nombre;
    private Integer cantidad;
    private String imagen;
    private List<ReencuentroInventarioDTO> reencuentros = new ArrayList<>();
}
