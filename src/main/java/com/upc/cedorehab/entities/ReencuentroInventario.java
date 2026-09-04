package com.upc.cedorehab.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reencuentro_inventario")
public class ReencuentroInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reencuentro_id")
    private Long reencuentroId;

    private Integer anio;
    private Integer mes;

    @Column(name = "numero_sabado")
    private Integer numeroSabado;

    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    @JsonBackReference("item-reencuentros")
    private InventarioItem item;

}
