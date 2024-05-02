package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequirementRequestDTO {
	@NotBlank(message = "Requirement description cannot be empty.")
	private String description;

	@NotNull(message = "Project id cannot be null.")
	private long project;
}
