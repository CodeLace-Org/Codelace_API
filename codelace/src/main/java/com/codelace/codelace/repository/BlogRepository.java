package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long>{
	Optional<Blog> findById(Optional<Long> id);
    Optional<Blog> findByTitle(String title);
}
