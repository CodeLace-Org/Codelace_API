package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long>{
	Optional<Plan>findById(Optional<Long>id);
	Optional<Plan>findByType(String type);
}
