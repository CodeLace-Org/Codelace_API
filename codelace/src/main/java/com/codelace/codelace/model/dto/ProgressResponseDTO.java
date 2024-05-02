package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponseDTO {
	private Long id;
	private Boolean completed;
	private String description;
}