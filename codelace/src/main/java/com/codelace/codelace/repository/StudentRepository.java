package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
	Optional<Student> findById(Optional<Long> id);
	Optional<Student> findByUsername(String username);
    Optional<Student> findByEmail(String email);
}
