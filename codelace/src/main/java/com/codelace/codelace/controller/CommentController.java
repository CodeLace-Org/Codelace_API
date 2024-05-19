package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.CommentRequestDTO;
import com.codelace.codelace.model.dto.CommentResponseDTO;
import com.codelace.codelace.service.CommentService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
	
}
