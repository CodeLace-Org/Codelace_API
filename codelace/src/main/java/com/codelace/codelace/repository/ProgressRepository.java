package com.codelace.codelace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.entity.Student;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
	Progress findByStudentAndRequirement(Student student, Requirement requirement);
}