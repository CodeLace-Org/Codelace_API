package com.codelace.codelace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;

import java.util.List;
import java.util.Optional;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
	Optional<Requirement> findById(Optional<Long> id);
	List<Requirement> findAllByProject(Project project);
}
