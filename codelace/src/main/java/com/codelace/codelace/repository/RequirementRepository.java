package com.codelace.codelace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelace.codelace.model.entity.Requirement;

public interface RequirementRepository extends JpaRepository<Requirement, Long>{
	
    @Query("SELECT t FROM Requirement t WHERE t.project=:projectID")
    List<Requirement> findByProjectID(@Param("projectID") Long projectID);
}
