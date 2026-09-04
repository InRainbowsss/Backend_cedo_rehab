package com.upc.cedorehab.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paciente_id")
    private Long pacienteId;

    @Column(nullable = false)
    private String nombre;

    private String edad;
    private String dni;
    private String celular;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    private String color;
    private String dniApoderado;

    @Column(name = "consulta_medica")
    private String consultaMedica;

    @Column(name = "costo_consulta")
    private BigDecimal costoConsulta;

    @Column(name = "costo_terapia")
    private BigDecimal costoTerapia;

    @Column(name = "tipo_atencion")
    private String tipoAtencion;

    @Column(name = "paquete_actual")
    private Integer paqueteActual;

    @Column(name = "reevaluacion_requerida")
    private Boolean reevaluacionRequerida = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "campo")
    private List<String> camposHC = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Boolean> boletasPaquetes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> camposHistoria;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("paciente-paquetes")
    private List<Paquete> paquetes = new ArrayList<>();

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("paciente-fichas")
    private List<FichaTerapia> fichasTerapias = new ArrayList<>();

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("paciente-historias")
    private List<HistoriaClinica> historiasClinicas = new ArrayList<>();

}
