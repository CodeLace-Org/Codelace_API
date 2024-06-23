package com.codelace.codelace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Comment;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findAllByPostAndStudent(Post posts, Student student);
	List<Comment> findAllByPost(Post post);
}
