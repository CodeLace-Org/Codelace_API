package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Ruta;

public interface RutaRepository extends JpaRepository<Ruta, Long> {
	Optional<Ruta> findById(Optional<Long> id);
}
