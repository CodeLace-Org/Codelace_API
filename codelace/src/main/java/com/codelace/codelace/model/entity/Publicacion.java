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
@Table(name = "publicaciones")
public class Publicacion {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="estudiantes_id", nullable = false)
    private Student estudiante;

    @ManyToOne
    @JoinColumn(name = "proyectos_id", nullable = false)
    private Proyecto proyecto;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "repo_url")
    private String repoUrl;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "imagen", nullable = false)
    private byte[] imagen;
}
