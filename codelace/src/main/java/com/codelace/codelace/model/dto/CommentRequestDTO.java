package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
	@NotNull(message = "Student ID is required.")
	private Long student;
	@NotNull(message = "Post ID is required.")
	private Long post;
	@NotBlank(message = "Comment content is required.")
	private String content;
}
