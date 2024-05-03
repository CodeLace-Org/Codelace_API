package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Comment;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	Optional<List<Comment>> findAllByPostAndStudent(Post posts, Student student);
}
