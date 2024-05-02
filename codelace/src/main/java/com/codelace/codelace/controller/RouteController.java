package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.codelace.codelace.model.dto.RouteResponseDTO;
import com.codelace.codelace.service.RouteService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RouteController {
	private final RouteService routeService;

	// Method that returns all the routes
	@GetMapping
	public ResponseEntity<List<RouteResponseDTO>> getAllRoutes() {
		List<RouteResponseDTO> routes = routeService.getAllRoutes();
		return new ResponseEntity<>(routes, HttpStatus.OK);
	}

	// Method that returns a route by its id
	@GetMapping("/{id}")
	public ResponseEntity<RouteResponseDTO> getRouteById(@PathVariable long id) {
		RouteResponseDTO route = routeService.getRouteById(id);
		return new ResponseEntity<>(route, HttpStatus.OK);
	}

	// Method that creates a route
	@PostMapping
	public ResponseEntity<RouteResponseDTO> createRoute(@Validated @RequestBody RouteRequestDTO routeRequestDTO) {
		RouteResponseDTO route = routeService.createRoute(routeRequestDTO);
		return new ResponseEntity<>(route, HttpStatus.CREATED);
	}

	// Method that updates a route
	@PutMapping("/{id}")
	public ResponseEntity<RouteResponseDTO> updateRoute(@PathVariable Long id, @RequestBody RouteRequestDTO routeRequestDTO) {
		RouteResponseDTO updatedRoute = routeService.updateRoute(id, routeRequestDTO);
		return new ResponseEntity<>(updatedRoute, HttpStatus.OK);
	}

	// Method that eliminates a route
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRoute(@PathVariable Long id){
		routeService.deleteRoute(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ---------------------------- ENDPOINTS

	//Method that gets all the projects by a routeId
	@GetMapping("/{id}/projects")
	public ResponseEntity<List<ProjectResponseDTO>> getProjectsByRoute(@PathVariable Long id){
		List<ProjectResponseDTO> projects = routeService.getProjectsByRoute(id);
		return new ResponseEntity<>(projects, HttpStatus.OK);
	}
}
