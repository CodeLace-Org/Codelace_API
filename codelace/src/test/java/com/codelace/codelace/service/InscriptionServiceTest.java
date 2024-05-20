package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

import com.codelace.codelace.exception.MaxFreeInscriptionException;
import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.exception.SubscriptionNotActiveException;
import com.codelace.codelace.mapper.InscriptionMapper;
import com.codelace.codelace.model.dto.InscriptionRequestDTO;
import com.codelace.codelace.model.dto.InscriptionResponseDTO;
import com.codelace.codelace.model.entity.Inscription;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.model.entity.Subscription;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.RouteRepository;
import com.codelace.codelace.repository.StudentRepository;
import com.codelace.codelace.repository.SubscriptionRepository;
import com.codelace.codelace.service.InscriptionService;

@ExtendWith(MockitoExtension.class)
public class InscriptionServiceTest {
    
    @Mock
    private InscriptionRepository inscriptionRepository;

    @Mock
    private InscriptionMapper inscriptionMapper;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private InscriptionService inscriptionService;

    @Test
    public void testGetAllInscriptions(){
        // Arrange
        Inscription inscription1 = new Inscription();
        inscription1.setId(1L);
        Inscription inscription2 = new Inscription();
        inscription2.setId(2L);
        List<Inscription> inscriptions = Arrays.asList(inscription1, inscription2);

        // Mocking repository
        when(inscriptionRepository.findAll()).thenReturn(inscriptions);

        // Mocking mapper
        InscriptionResponseDTO responseDTO1 = new InscriptionResponseDTO();
        InscriptionResponseDTO responseDTO2 = new InscriptionResponseDTO();
        responseDTO1.setId(inscription1.getId());
        responseDTO2.setId(inscription2.getId());

        List<InscriptionResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);
        when(inscriptionMapper.convertToListDTO(inscriptions)).thenReturn(expectedResponse);

        // Act
        List<InscriptionResponseDTO> result = inscriptionService.getAllInscriptions();

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());

        // Verify that repository and mapper methods were called
        verify(inscriptionRepository, times(1)).findAll();
        verify(inscriptionMapper, times(1)).convertToListDTO(inscriptions);
    }

    @Test
    public void testGetInscriptionById_ExistingId(){
        // Arrange
        Long id = 1L;
        Inscription inscription = new Inscription();
        inscription.setId(id);
        when(inscriptionRepository.findById(id)).thenReturn(Optional.of(inscription));

        // Mocking mapper
        InscriptionResponseDTO responseDTO = new InscriptionResponseDTO();
        responseDTO.setId(inscription.getId());
        when(inscriptionMapper.convertInscriptionToResponse(inscription)).thenReturn(responseDTO);

        // Act
        InscriptionResponseDTO result = inscriptionService.getInscriptionById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }
    
    @Test
    public void testGetInscriptionById_NonExistingId(){
        // Arrange
        Long id = 999L;
        when(inscriptionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> inscriptionService.getInscriptionById(id));
    }

    @Test
    public void testCreateInscription_SuccessfullInscription(){
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        Route route = new Route();
        route.setId(1L);
        route.setDescription("description");
        route.setIcon("icon");
        route.setName("route name");

        Student student = new Student();
        student.setId(1L);

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setType("Gratis");

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setActive(true);
        subscription.setPlan(plan);
        subscription.setStudent(student);

        // Mocking repository
        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
        when(routeRepository.findById(requestDTO.getRoute())).thenReturn(Optional.of(route));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.empty());
        when(subscriptionRepository.findById(student.getId())).thenReturn(Optional.of(subscription));
        when(inscriptionRepository.findByStudent(student)).thenReturn(Optional.empty());

        // Mocking mapper
        Inscription inscription = new Inscription();
        inscription.setId(1L);
        inscription.setRoute(route);
        inscription.setStudent(student);
        when(inscriptionMapper.convertInscriptionRequestToEntity(requestDTO)).thenReturn(inscription);

        // Mocking repository
        when(inscriptionRepository.save(inscription)).thenReturn(inscription);

        // Mocking mapper
        InscriptionResponseDTO responseDTO = new InscriptionResponseDTO();
        responseDTO.setId(inscription.getId());
        responseDTO.setRoute(route);
        responseDTO.setStudentId(student.getId());
        when(inscriptionMapper.convertInscriptionToResponse(inscription)).thenReturn(responseDTO);

        // Act
        InscriptionResponseDTO result = inscriptionService.createInscription(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(inscription.getId(), result.getId());
        assertEquals(student.getId(), result.getStudentId());
        assertEquals(route, result.getRoute());

        // Verify that repository and mapper methods were called
        verify(studentRepository, times(1)).findById(1L);
        verify(routeRepository, times(1)).findById(1L);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
        verify(subscriptionRepository, times(1)).findById(1L);
        verify(inscriptionRepository, times(1)).findByStudent(student);
        verify(inscriptionRepository, times(1)).save(inscription);
        verify(inscriptionMapper, times(1)).convertInscriptionRequestToEntity(requestDTO);
        verify(inscriptionMapper, times(1)).convertInscriptionToResponse(inscription);
    }

    @Test
    public void testCreateInscription_StudentNotFound(){
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        // Mock repository
        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> inscriptionService.createInscription(requestDTO));

        // Verify that repository and mapper methods were called
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateInscription_RouteNotFound(){
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        // Simulate that student exists but route doesn't
        Student student = new Student();
        student.setId(1L);

        // Mock Repository
        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
        when(routeRepository.findById(requestDTO.getRoute())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> inscriptionService.createInscription(requestDTO));
        
        // Verify that repository and mapper methods were called
        verify(studentRepository, times(1)).findById(1L);
        verify(routeRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateInscription_InscriptionAlreadyExists() {
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        Route route = new Route();
        route.setId(1L);
        route.setDescription("description");
        route.setIcon("icon");
        route.setName("route name");

        Student student = new Student();
        student.setId(1L);

        Inscription inscription = new Inscription();
        inscription.setId(1L);
        inscription.setRoute(route);
        inscription.setStudent(student);

        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
        when(routeRepository.findById(requestDTO.getRoute())).thenReturn(Optional.of(route));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.of(inscription));

        assertThrows(ResourceDuplicateException.class, () -> inscriptionService.createInscription(requestDTO));
        
        // Verify that repository and mapper methods were called
        verify(studentRepository, times(1)).findById(1L);
        verify(routeRepository, times(1)).findById(1L);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
    }

    @Test
    public void testCreateInscription_SubscriptionNotFound(){
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        Route route = new Route();
        route.setId(1L);
        route.setDescription("description");
        route.setIcon("icon");
        route.setName("route name");

        Student student = new Student();
        student.setId(1L);

        Inscription inscription = new Inscription();
        inscription.setId(1L);
        inscription.setRoute(route);
        inscription.setStudent(student);

        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
        when(routeRepository.findById(requestDTO.getRoute())).thenReturn(Optional.of(route));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.empty());
        when(subscriptionRepository.findById(student.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> inscriptionService.createInscription(requestDTO));

        // Verify that repository and mapper methods were called
        verify(studentRepository, times(1)).findById(1L);
        verify(routeRepository, times(1)).findById(1L);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateInscription_SubscriptionNotActive(){
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        Route route = new Route();
        route.setId(1L);
        route.setDescription("description");
        route.setIcon("icon");
        route.setName("route name");

        Student student = new Student();
        student.setId(1L);

        Inscription inscription = new Inscription();
        inscription.setId(1L);
        inscription.setRoute(route);
        inscription.setStudent(student);

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setType("Gratis");

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setActive(false);
        subscription.setPlan(plan);
        subscription.setStudent(student);

        // Mocking repository
        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
        when(routeRepository.findById(requestDTO.getRoute())).thenReturn(Optional.of(route));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.empty());
        when(subscriptionRepository.findById(student.getId())).thenReturn(Optional.of(subscription));

        // Act & Assert
        assertThrows(SubscriptionNotActiveException.class, () -> inscriptionService.createInscription(requestDTO));

        verify(studentRepository, times(1)).findById(1L);
        verify(routeRepository, times(1)).findById(1L);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateInscription_MaxFreeInscription(){
        // Arrange
        InscriptionRequestDTO requestDTO = new InscriptionRequestDTO();
        requestDTO.setRoute(1L);
        requestDTO.setStudent(1L);

        Route route = new Route();
        route.setId(1L);
        route.setDescription("description");
        route.setIcon("icon");
        route.setName("route name");

        Student student = new Student();
        student.setId(1L);

        Inscription inscription = new Inscription();
        inscription.setId(1L);
        inscription.setRoute(route);
        inscription.setStudent(student);

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setType("Gratis");

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setActive(true);
        subscription.setPlan(plan);
        subscription.setStudent(student);

        // Mocking repository
        when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
        when(routeRepository.findById(requestDTO.getRoute())).thenReturn(Optional.of(route));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.empty());
        when(subscriptionRepository.findById(student.getId())).thenReturn(Optional.of(subscription));
        when(inscriptionRepository.findByStudent(student)).thenReturn(Optional.of(inscription));

        assertThrows(MaxFreeInscriptionException.class, () -> inscriptionService.createInscription(requestDTO));
    }

    @Test
    public void testDeleteInscription_SuccessfullDelete(){
        // Arrange
        Long id = 1L;
        Inscription inscription = new Inscription();
        inscription.setId(id);

        when(inscriptionRepository.findById(id)).thenReturn(Optional.of(inscription));

        // Act
        assertDoesNotThrow(() -> inscriptionService.deleteInscription(id));

        // Assert
        verify(inscriptionRepository, times(1)).delete(inscription);
    }

    @Test
    public void testDeleteInscription_InscriptionNotFound(){
        // Arrange
        Long id = 1L;

        when(inscriptionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> inscriptionService.deleteInscription(id));
    }
}
