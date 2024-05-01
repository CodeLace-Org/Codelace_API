package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.RouteMapper;
import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.codelace.codelace.model.dto.RouteResponseDTO;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.repository.RouteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RouteService {
	private final RouteRepository routeRepository;
	private final RouteMapper routeMapper;

	// Method that returns all the routes
	public List<RouteResponseDTO> getAllRoutes() {
		List<Route> routes = routeRepository.findAll();
		return routeMapper.convertToListDTO(routes);
	}

	// Method that returns a route by its id
	public RouteResponseDTO getRouteById(Long id) {
		Route route = routeRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Route not found."));
		return routeMapper.convertToDTO(route);
	}

	// Method that creates a route
	public RouteResponseDTO createRoute(RouteRequestDTO routeRequestDTO) {
		Route route = routeMapper.convertToEntity(routeRequestDTO);
		routeRepository.save(route);
		return routeMapper.convertToDTO(route);
	}

	// Method that updates a route
	public RouteResponseDTO updateRoute(Long id, RouteRequestDTO routeRequestDTO) {
		Route route = routeRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Route not found."));
		route.setName(routeRequestDTO.getName());
		route.setDescription(routeRequestDTO.getDescription());
		route.setIcon(routeRequestDTO.getIcon());
		routeRepository.save(route);
		return routeMapper.convertToDTO(route);
	}

	// Method that deletes a route
	public void deleteRoute(Long id) {
		Route route = routeRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Route not found."));
		routeRepository.delete(route);
	}

}
