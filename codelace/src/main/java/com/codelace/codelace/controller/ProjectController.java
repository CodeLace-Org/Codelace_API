package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.service.PostService;
import com.codelace.codelace.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

	private final ProjectService projectService;
	private final PostService postService;
	// Method that returns all the projects
	@GetMapping
	public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
		List<ProjectResponseDTO> projects = projectService.getAllProjects();
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}

	// Method that returns a project by its id
	@GetMapping("/{id}")
	public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
		ProjectResponseDTO project = projectService.getProjectById(id);
		return new ResponseEntity<>(project, HttpStatus.OK);
	}

	// Method that creates a project
	@PostMapping
	public ResponseEntity<ProjectResponseDTO> createProject(@Validated @RequestBody ProjectRequestDTO projectRequestDTO) {
		ProjectResponseDTO project = projectService.createProject(projectRequestDTO);
		return new ResponseEntity<>(project, HttpStatus.CREATED);
	}

	// Method that deletes a project
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		projectService.deleteProject(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/{id}/posts")
	public ResponseEntity<List<PostResponseDTO>> getPostsByProjectId(@PathVariable Long id){
		List<PostResponseDTO> posts = postService.getPostsByProjectId(id);
		return new ResponseEntity<>(posts, HttpStatus.OK);
	}
}
