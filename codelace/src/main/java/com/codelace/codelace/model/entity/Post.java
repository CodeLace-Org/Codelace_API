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
@Table(name = "posts")
public class Post {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="students_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "proyects_id", nullable = false)
    private Proyect proyect;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "repo_url")
    private String repoUrl;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private byte[] image;
}
