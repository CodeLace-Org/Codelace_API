package com.codelace.codelace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelace.codelace.model.entity.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long>{
	
    @Query("SELECT t from Progress t WHERE t.requirement=:requirementID AND t.student=:studentID")
    List<Progress> findByRequirementIDAndStudentID(@Param("requirementID") Long requirementID, @Param("studentID") Long studentID);
}
