package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Rocket;
import com.codelace.codelace.model.entity.Student;

public interface RocketRepository extends JpaRepository<Rocket, Long> {
	List<Rocket> findAllByPost(Post post);
	List<Rocket> findAllByPostAndStudent(Post posts, Student student);
	Optional<Rocket> findByPostIdAndStudentId(Long post_id, Long student_id);
}
