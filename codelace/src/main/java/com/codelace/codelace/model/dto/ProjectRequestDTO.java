package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {
	@NotNull(message = "Route ID is required.")
	private long route;

	@NotBlank(message = "Route title cannot be empty.")
	private String title;

	@NotBlank(message = "Route description cannot be empty.")
	private String description;

	private byte[] image;
	
	@NotNull(message = "Level number cannot be null.")
	private int level;
}
