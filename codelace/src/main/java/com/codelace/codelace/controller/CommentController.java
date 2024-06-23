package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.CommentRequestDTO;
import com.codelace.codelace.model.dto.CommentResponseDTO;
import com.codelace.codelace.service.CommentService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
	// Instance of service
	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO) {
		CommentResponseDTO response = commentService.createComment(commentRequestDTO);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	@GetMapping("/post/{post_id}")
	public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByPostId(@PathVariable Long post_id) {
		List<CommentResponseDTO> response = commentService.getAllCommentsByPostId(post_id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
}
