package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Base64;
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
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.codelace.codelace.model.dto.RouteResponseDTO;
import com.codelace.codelace.model.entity.Project;
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

	@Test
	public void getProjectByRouteTest_ExistingId(){
		// Arrange
		Long routeID = 1L;

		Project project1 = new Project();
		project1.setId(1L);
		project1.setTitle("Titulo de proyecto1");
		project1.setDescription("Descripcion de proyecto1");
		String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
		project1.setImage(Base64.getDecoder().decode(base64Image));
		project1.setLevel(1);

		Project project2 = new Project();
		project2.setId(1L);
		project2.setTitle("Titulo de proyecto1");
		project2.setDescription("Descripcion de proyecto1");
		String base64Image2 = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
		project2.setImage(Base64.getDecoder().decode(base64Image2));
		project2.setLevel(1);

		List<Project> projects = Arrays.asList(project1, project2);

		// Mocking Repository
		when(projectRepository.findAllByRouteId(routeID)).thenReturn(Optional.of(projects));

		// Mocking mapper
		ProjectResponseDTO responseDTO = new ProjectResponseDTO();
		ProjectResponseDTO responseDTO2 = new ProjectResponseDTO();
		responseDTO.setId(project1.getId());
		responseDTO2.setId(project2.getId());

		List<ProjectResponseDTO> expectedResponse = Arrays.asList(responseDTO, responseDTO2);
		when(projectMapper.convertToListDTO(projects)).thenReturn(expectedResponse);

		// Act
		List<ProjectResponseDTO> result = routeService.getProjectsByRoute(routeID);

		// Assert
		assertNotNull(result);
		assertEquals(expectedResponse.size(), result.size());

		// Verify that repository and mapper methods were called
		verify(projectRepository, times(1)).findAllByRouteId(routeID);
		verify(projectMapper, times(1)).convertToListDTO(projects);
	}

	@Test
	public void getProjectByRouteTest_NonExistingId() {
		// Arrange
		Long routeID = 999L;

		// Configurar el comportamiento simulado del repositorio
		when(projectRepository.findAllByRouteId(routeID)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> routeService.getProjectsByRoute(routeID));

		// Verify
		verify(projectRepository, times(1)).findAllByRouteId(routeID);
	}
}
