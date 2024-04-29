package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Route;

public interface RouteRepository extends JpaRepository<Route, Long> {
	Optional<Route> findById(Optional<Long> id);
}
