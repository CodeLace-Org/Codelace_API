package com.codelace.codelace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	
}
