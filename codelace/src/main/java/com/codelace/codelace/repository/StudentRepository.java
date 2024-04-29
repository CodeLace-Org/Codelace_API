package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Estudiante;

public interface StudentRepository extends JpaRepository<Estudiante, Long>{
	Optional<Estudiante> findById(Optional<Long> id);
	Optional<Estudiante> findByUsername(String usuario);
    Optional<Estudiante> findByEmail(String email);
}
