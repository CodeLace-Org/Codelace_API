package com.codelace.codelace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelace.codelace.model.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long>{

	@Query("SELECT t FROM Project t WHERE t.route=:routeID")
	List<Project> findProjectsByRoute(@Param("routeID") Long routeID);
}
