package com.codelace.codelace.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailsResponseDTO {

	private Long id;

	private String title;

	private String description;

	private byte[] image;

	private int level;

	private List<ProgressResponseDTO> progress;

}
