package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.RutaRequestDTO;
import com.codelace.codelace.model.dto.RutaResponseDTO;
import com.codelace.codelace.service.RutaService;

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
public class RutaController {
	private final RutaService rutaService;

	// Method that returns all the routes
	@GetMapping
	public ResponseEntity<List<RutaResponseDTO>> getAllRoutes() {
		List<RutaResponseDTO> rutas = rutaService.getAllRoutes();
		return new ResponseEntity<>(rutas, HttpStatus.OK);
	}

	// Method that returns a route by its id
	@GetMapping("/{id}")
	public ResponseEntity<RutaResponseDTO> getRouteById(@PathVariable long id) {
		RutaResponseDTO ruta = rutaService.getRouteById(id);
		return new ResponseEntity<>(ruta, HttpStatus.OK);
	}

	// Method that creates a route
	@PostMapping
	public ResponseEntity<RutaResponseDTO> createRoute(@Validated @RequestBody RutaRequestDTO rutaRequestDTO) {
		RutaResponseDTO ruta = rutaService.createRoute(rutaRequestDTO);
		return new ResponseEntity<>(ruta, HttpStatus.CREATED);
	}

}
