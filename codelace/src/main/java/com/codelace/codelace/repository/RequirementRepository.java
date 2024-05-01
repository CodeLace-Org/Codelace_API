package com.codelace.codelace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;

public interface RequirementRepository extends JpaRepository<Requirement, Long>{
	
    List<Requirement> findAllByProject(Project project);
}
