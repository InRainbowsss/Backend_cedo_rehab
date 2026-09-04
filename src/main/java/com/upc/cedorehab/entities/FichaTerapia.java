package com.upc.cedorehab.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fichas_terapias")
public class FichaTerapia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ficha_id")
    private Long fichaId;

    @Column(name = "numero_ficha")
    private Integer numeroFicha;

    @Column(name = "paquete_num")
    private Integer paqueteNum;

    private String titulo;

    private LocalDate fecha;

    @Column(name = "fecha_dx")
    private LocalDate fechaDx;

    private String dx;

    @Column(name = "tipo_atencion")
    private String tipoAtencion;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> terapias = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> indicacionesExtra = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    @JsonBackReference("paciente-fichas")
    private Paciente paciente;

}
