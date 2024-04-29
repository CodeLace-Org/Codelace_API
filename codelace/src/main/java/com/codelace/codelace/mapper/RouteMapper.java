package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.codelace.codelace.model.dto.RouteResponseDTO;
import com.codelace.codelace.model.entity.Route;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RouteMapper {
	private final ModelMapper modelMapper;

	public Route convertToEntity(RouteRequestDTO routeRequestDTO) {
		return modelMapper.map(routeRequestDTO, Route.class);
	}

	public RouteResponseDTO convertToDTO(Route route) {
		return modelMapper.map(route, RouteResponseDTO.class);
	}

	public List<RouteResponseDTO> convertToListDTO(List<Route> routes) {
		return routes.stream()
				.map(this::convertToDTO)
				.toList();
	}
}
