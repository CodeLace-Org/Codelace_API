package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.codelace.codelace.model.dto.RouteResponseDTO;
import com.codelace.codelace.service.RouteService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/rutas")
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

}
