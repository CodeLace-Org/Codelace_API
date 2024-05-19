package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
	Optional<Resource> findById(Optional<Long> id);

	List<Resource> findAllByProject(Project project);
}
