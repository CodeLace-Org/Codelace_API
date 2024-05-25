package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.mapper.RouteMapper;
import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.codelace.codelace.model.dto.RouteResponseDTO;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RouteRepository;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {
	@Mock
	private RouteRepository routeRepository;

	@Mock
	private ProjectRepository projectRepository;

	@Mock
	private RouteMapper routeMapper;

	@Mock
	private ProjectMapper projectMapper;

	@InjectMocks
	private RouteService routeService;

	@Test
	public void getAllRoutesTest() {
		// Arrange
		Route route1 = new Route();
		route1.setId(1L);
		route1.setName("web");
		route1.setDescription("desarrollo web");
		route1.setIcon("<svg>icon</svg>");
		Route route2 = new Route();
		route2.setId(2L);
		route2.setName("mobile");
		route2.setDescription("desarrollo mobile");
		route2.setIcon("<svg>icon</svg>");

		List<Route> routes = Arrays.asList(route1, route2);

		// Mocking repository
		when(routeRepository.findAll()).thenReturn(routes);

		// Mockin Mapper
		RouteResponseDTO routeResponseDTO1 = new RouteResponseDTO();
		routeResponseDTO1.setId(1L);
		routeResponseDTO1.setName("web");
		routeResponseDTO1.setDescription("desarrollo web");
		routeResponseDTO1.setIcon("<svg>icon</svg>");
		RouteResponseDTO routeResponseDTO2 = new RouteResponseDTO();
		routeResponseDTO2.setId(2L);
		routeResponseDTO2.setName("mobile");
		routeResponseDTO2.setDescription("desarrollo mobile");
		routeResponseDTO2.setIcon("<svg>icon</svg>");

		List<RouteResponseDTO> expected = Arrays.asList(routeResponseDTO1, routeResponseDTO2);
		when(routeMapper.convertToListDTO(routes)).thenReturn(expected);
		// Act
		List<RouteResponseDTO> response = routeService.getAllRoutes();

		// Assert
		assertNotNull(response);
		assertEquals(expected.size(), response.size());

		// Verify
		verify(routeRepository, times(1)).findAll();
		verify(routeMapper, times(1)).convertToListDTO(routes);
	}

	@Test
	public void getRouteByIdTest() {
		// Arrange
		Route route = new Route();
		route.setId(1L);
		when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

		RouteResponseDTO expected = new RouteResponseDTO();
		expected.setId(1L);
		when(routeMapper.convertToDTO(route)).thenReturn(expected);

		// Act
		RouteResponseDTO response = routeService.getRouteById(1L);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(routeRepository, times(1)).findById(1L);
		verify(routeMapper, times(1)).convertToDTO(route);
	}

	@Test
	public void getRouteByIdTest_RouteNotFound() {
		// Arrange
		Long id = 1L;
		when(routeRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> routeService.getRouteById(id));
	}

	@Test
	public void createRouteTest() {
		// Arrange
		RouteRequestDTO routeRequestDTO = new RouteRequestDTO();
		Route route = new Route();
		when(routeMapper.convertToEntity(routeRequestDTO)).thenReturn(route);
		when(routeRepository.save(route)).thenReturn(route);

		RouteResponseDTO expected = new RouteResponseDTO();
		when(routeMapper.convertToDTO(route)).thenReturn(expected);

		// Act
		RouteResponseDTO response = routeService.createRoute(routeRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(routeMapper, times(1)).convertToEntity(routeRequestDTO);
		verify(routeRepository, times(1)).save(route);
		verify(routeMapper, times(1)).convertToDTO(route);
	}

	@Test
	public void updateRouteTest() {
		// Arrange
		RouteRequestDTO routeRequestDTO = new RouteRequestDTO();
		Route route = new Route();
		when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
		when(routeRepository.save(route)).thenReturn(route);

		RouteResponseDTO expected = new RouteResponseDTO();
		when(routeMapper.convertToDTO(route)).thenReturn(expected);

		// Act
		RouteResponseDTO response = routeService.updateRoute(1L, routeRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(routeRepository, times(1)).findById(1L);
		verify(routeRepository, times(1)).save(route);
		verify(routeMapper, times(1)).convertToDTO(route);
	}

	@Test
	public void updateRouteTest_RouteNotFound() {
		// Arrange
		Long id = 1L;
		when(routeRepository.findById(id)).thenReturn(Optional.empty());

		RouteRequestDTO routeRequestDTO = new RouteRequestDTO();
		routeRequestDTO.setName("web");
		routeRequestDTO.setDescription("desarrollo web");
		routeRequestDTO.setIcon("<svg>icon</svg>");

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> routeService.updateRoute(id, routeRequestDTO));
	}

	@Test
	public void deleteRouteTest() {
		// Arrange
		Route route = new Route();
		when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

		// Act
		routeService.deleteRoute(1L);

		// Verify
		verify(routeRepository, times(1)).findById(1L);
		verify(routeRepository, times(1)).delete(route);
	}

	@Test
	public void deleteRouteTest_RouteNotFound() {
		// Arrange
		Long id = 1L;
		when(routeRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> routeService.deleteRoute(id));
	}

	// TODO -> Add test for getProjectsByRouteTest
}
