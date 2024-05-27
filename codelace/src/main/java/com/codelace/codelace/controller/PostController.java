package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostByStudentResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.dto.PostResponseIdDTO;
import com.codelace.codelace.service.PostService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {
	private final PostService postService;

	// ---------------------------- ENDPOINTS (CRUD)
	@GetMapping("/{id}")
	public ResponseEntity<PostResponseIdDTO> getPostById(@PathVariable Long id) {
		PostResponseIdDTO post = postService.getPostById(id);
		return new ResponseEntity<>(post, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<PostByProjectResponseDTO>> getAllPosts() {
		List<PostByProjectResponseDTO> posts = postService.getAllPosts();
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<PostResponseDTO> createPost(@Validated @RequestBody PostRequestDTO postDTO) {
		PostResponseDTO createdPost = postService.createPost(postDTO);
		return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
	}

	// ---------------------------- ENDPOINTS
	@GetMapping("/students/{student_id}")
	public ResponseEntity<List<PostByStudentResponseDTO>> getAllPostsByStudent(@PathVariable Long student_id) {
		List<PostByStudentResponseDTO> response = postService.getAllPostsByStudent(student_id);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}	
	
	@GetMapping("/projects/{project_id}")
	public ResponseEntity<List<PostByProjectResponseDTO>> getAllPostsByProjectId(@PathVariable Long project_id) {
		List<PostByProjectResponseDTO> response = postService.getAllPostsByProjectId(project_id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/students/{student_id}/projects/{project_id}")
	public ResponseEntity<PostByStudentResponseDTO> getPostByStudentAndProject(@PathVariable Long student_id, @PathVariable Long project_id) {
		PostByStudentResponseDTO response = postService.getPostByStudentAndProject(student_id, project_id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}