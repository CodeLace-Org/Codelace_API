package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.entity.Student;

public interface ProgressRepository extends JpaRepository<Progress, Long>{
    Optional<Progress> findByStudentAndRequirement(Student student, Requirement requirement);
}
