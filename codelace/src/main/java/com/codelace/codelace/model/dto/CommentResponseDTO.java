package com.codelace.codelace.model.dto;

import java.time.LocalDate;
import com.codelace.codelace.model.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
	private String id;
	private Student student;
	private String content;
	private LocalDate date;
}
