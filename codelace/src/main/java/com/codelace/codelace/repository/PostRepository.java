package com.codelace.codelace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	Optional<List<Post>> findByProjectId(Long id);
}
