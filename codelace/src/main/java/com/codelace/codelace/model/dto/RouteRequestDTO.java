package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequestDTO {
	@NotBlank(message = "Route name cannot be empty.")
	private String name;

	@NotBlank(message = "Route description cannot be empty.")
	private String description;

	@NotBlank(message = "Route icon (svg/xml) cannot be empty.")
	private String icon;
}
