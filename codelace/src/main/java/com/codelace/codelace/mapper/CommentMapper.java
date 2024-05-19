package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.codelace.codelace.model.dto.CommentRequestDTO;
import com.codelace.codelace.model.dto.CommentResponseDTO;
import com.codelace.codelace.model.entity.Comment;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommentMapper {
	private final ModelMapper modelMapper;

	// Request Blog DTO to Entity
	public Comment convertToEntity(CommentRequestDTO commentRequestDTO) {
		return modelMapper.map(commentRequestDTO, Comment.class);
	}

	// Blog Entity to Response DTO
	public CommentResponseDTO convertToResponse(Comment comment) {
		return modelMapper.map(comment, CommentResponseDTO.class);
	}

	// List Blog Entity to List response DTO
	public List<CommentResponseDTO> convertToResponse(List<Comment> comments) {
		return comments.stream()
				.map(this::convertToResponse)
				.toList();
	}
}
