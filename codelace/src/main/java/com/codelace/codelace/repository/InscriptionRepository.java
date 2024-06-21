package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Inscription;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.model.entity.Student;


public interface InscriptionRepository extends JpaRepository<Inscription, Long>{
    Optional<Inscription> findById(Optional<Long> id);
    Optional<Inscription> findByRoute(Route route);
    Optional<Inscription> findByStudent(Student student);
    List<Inscription> findAllByStudent(Student student);
    Optional<Inscription> findByStudentAndRoute(Student student, Route route);
}
