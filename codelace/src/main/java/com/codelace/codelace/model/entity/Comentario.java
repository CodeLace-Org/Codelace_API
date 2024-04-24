package com.codelace.codelace.model.entity;

import java.sql.Date;

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
@Table(name = "comentarios")
public class Comentario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="estudiantes_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name="publicaciones_id", nullable = false)
    private Publicacion publicacion;

    @Column(name="contenido", nullable = false)
    private String contenido;

    @Column(name="fecha", nullable = false)
    private Date fecha;
}
