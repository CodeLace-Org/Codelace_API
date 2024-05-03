package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.BlogRequestDTO;
import com.codelace.codelace.model.dto.BlogResponseDTO;
import com.codelace.codelace.service.BlogService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/blogs")
@AllArgsConstructor
public class BlogController {
	private final BlogService blogService;

    // ---------------------------- ENDPOINTS (CRUD)
    // Method that returns all the blogs
    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs() {
        List<BlogResponseDTO> blogs = blogService.getAllBlogs();
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    // Method that returns a blog by its id
    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> getBlogById(@PathVariable Long id) {
        BlogResponseDTO blog = blogService.getBlogById(id);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    // Method that creates a blog
    @PostMapping
    public ResponseEntity<BlogResponseDTO> createBlog(
            @Validated @RequestBody BlogRequestDTO blogRequestDTO) {
        BlogResponseDTO blog = blogService.createBlog(blogRequestDTO);
        return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }
    
    // Method that updates a blog
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> updateBlog(@PathVariable Long id, @RequestBody BlogRequestDTO blogRequestDTO) {
        BlogResponseDTO blog = blogService.updateBlog(id, blogRequestDTO);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    // Method that deletes a blog
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
