package com.codelace.codelace.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.CommentMapper;
import com.codelace.codelace.model.dto.CommentRequestDTO;
import com.codelace.codelace.model.dto.CommentResponseDTO;
import com.codelace.codelace.model.entity.Comment;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.CommentRepository;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {
	// Instance mapper
	private final CommentMapper commentMapper;
	// Instances of the repositories
	private final CommentRepository commentRepository;
	private final StudentRepository studentRepository;
	private final PostRepository postRepository;

	// Method that creates a comment
	public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
		Student student = studentRepository
				.findById(commentRequestDTO.getStudent())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found."));

		Post post = postRepository
				.findById(commentRequestDTO.getPost())
				.orElseThrow(() -> new ResourceNotFoundException("Post not found."));

		Comment comment = commentMapper.convertToEntity(commentRequestDTO);
		LocalDate date = LocalDate.now();
		comment.setDate(date);
		comment.setPost(post);
		comment.setStudent(student);
		comment = commentRepository.save(comment);
		return commentMapper.convertToResponse(comment);
	}
}
