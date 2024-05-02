package com.codelace.codelace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codelace.codelace.model.entity.Project;
import java.util.Optional;
public interface ProjectRepository extends JpaRepository<Project, Long> {
	Optional<Project> findById(Optional<Long> id);
}
