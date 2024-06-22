package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Student;

public interface PostRepository extends JpaRepository<Post, Long>{
	@Query("SELECT p.id AS id, p.student AS student, (SELECT COUNT(r) FROM Rocket r WHERE r.post = p AND r.student = p.student) AS rockets, (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.student = p.student) AS comments, p.date AS date, p.image AS image FROM Post p")
	List<Object[]> findAllPosts();
	Optional<List<Post>> findAllByProject(Project project);
	@Query("SELECT p.id AS id, p.student AS student, p.project.title AS title, p.demoUrl, p.repoUrl, p.description, p.date, p.image FROM Post p WHERE p.student = :student ORDER BY p.date DESC")
	Optional<List<Object[]>> findAllByStudent(@Param("student") Student student);
	@Query("SELECT p.id AS id, p.student AS student, (SELECT COUNT(r) FROM Rocket r WHERE r.post = p AND r.student = p.student) AS rockets, (SELECT COUNT(c) FROM Comment c WHERE c.post = p AND c.student = p.student) AS comments, p.date AS date, p.image AS image FROM Post p WHERE p.project.id = :projectId")
	Optional<List<Object[]>> findAllByProjectId(@Param("projectId") Long projectId);
	Optional<Post> findByStudentIdAndProjectId(Long studentId, Long projectId);
}
