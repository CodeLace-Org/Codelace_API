package com.codelace.codelace.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "students")
public class Student {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name = "status")
    private String status;

    @Column(name="description")
    private String description;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="pwd", nullable = false)
    private String pwd;

    @Column(name = "profile_picture")
    private byte[] profile_picture;
}
