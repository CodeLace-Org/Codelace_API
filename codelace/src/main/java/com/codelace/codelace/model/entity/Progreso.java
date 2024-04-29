package com.codelace.codelace.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requisitos_estudiantes")
public class Progreso {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="estudiantes_id", nullable = false)
    private Student estudiante;

    @ManyToOne
    @JoinColumn(name="requisitos_id", nullable = false)
    private Requisito requisito;

    @Column(name = "completado", nullable = false)
    private boolean completado;
}
