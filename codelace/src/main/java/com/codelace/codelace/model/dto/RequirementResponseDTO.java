package com.codelace.codelace.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequirementResponseDTO {
	private Long id;
	private String description;
	private Long projectId;
}
