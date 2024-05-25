package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.SubscriptionMapper;
import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.model.entity.Subscription;
import com.codelace.codelace.repository.PlanRepository;
import com.codelace.codelace.repository.StudentRepository;
import com.codelace.codelace.repository.SubscriptionRepository;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @Mock
    private SubscriptionMapper subscriptionMapper;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    public void testGetAllSubscriptions() {

        // Arrange

        Subscription subscription1 = new Subscription();
        Subscription subscription2 = new Subscription();
        subscription1.setId(1L);
        subscription2.setId(1L);

        List<Subscription> subscriptions = Arrays.asList(subscription1, subscription2);

        // Mocking repository
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);

        SubscriptionResponseDTO responseDTO1 = new SubscriptionResponseDTO();
        SubscriptionResponseDTO responseDTO2 = new SubscriptionResponseDTO();
        responseDTO1.setId(subscription1.getId());
        responseDTO2.setId(subscription2.getId());

        List<SubscriptionResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);

        when(subscriptionMapper.convertToListDTO(subscriptions)).thenReturn(expectedResponse);

        // Act
        List<SubscriptionResponseDTO> result = subscriptionService.getAllSubscriptions();

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());

        // Verify
        verify(subscriptionRepository, times(1)).findAll();
        verify(subscriptionMapper, times(1)).convertToListDTO(subscriptions);
    }

    @Test
    public void testGetSubscriptionById_ExistingId() {

        //Arrange
        Long id = 1L;
        Subscription subscription = new Subscription();
        subscription.setId(1L);

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));

        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO();
        responseDTO.setId(subscription.getId());
        when(subscriptionMapper.convertEntityToResponse(subscription)).thenReturn(responseDTO);

        // Act
        SubscriptionResponseDTO result = subscriptionService.getSubscriptionById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());

        // Verify
        verify(subscriptionRepository, times(1)).findById(id);
        verify(subscriptionMapper, times(1)).convertEntityToResponse(subscription);
    }

    @Test
    public void testGetSubscriptionById_SubscriptionNotFound() {

        // Arrange
        Long id = 1L;

        when(subscriptionRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> subscriptionService.getSubscriptionById(id));
    }

    @Test
    public void testCreateSubscription_Successfull() {

        // Arrange
        Student student = new Student();
        student.setId(1L);

        when(subscriptionRepository.findByStudent(student)).thenReturn(Optional.empty());

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setType("Gratis");

        when(planRepository.findByType("Gratis")).thenReturn(Optional.of(plan));

        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusMonths(1);

        Subscription subscription = new Subscription();
        subscription.setStudent(student);
        subscription.setActive(true);
        subscription.setBeginDate(beginDate);
        subscription.setEndDate(endDate);
        subscription.setPlan(plan);

        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        SubscriptionResponseDTO subscriptionResponseDTO = new SubscriptionResponseDTO();
        subscriptionResponseDTO.setId(subscription.getId());

        when(subscriptionMapper.convertEntityToResponse(subscription)).thenReturn(subscriptionResponseDTO);

        // Act
        SubscriptionResponseDTO result = subscriptionService.createSubscription(student);

        // Assert
        assertNotNull(result);
        assertEquals(subscription.getId(), result.getId());

        // Verify
        verify(subscriptionRepository, times(1)).findByStudent(student);
        verify(planRepository, times(1)).findByType("Gratis");
        verify(subscriptionRepository, times(1)).save(subscription);
        verify(subscriptionMapper, times(1)).convertEntityToResponse(subscription);

    }

    @Test
    public void testCreateSubscription_SubscriptionAlreadyExists() {
        // Arrange
        Student student = new Student();
        student.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setStudent(student);

        when(subscriptionRepository.findByStudent(student)).thenReturn(Optional.of(subscription));

        // Act & Assert
        assertThrows(ResourceDuplicateException.class, () -> subscriptionService.createSubscription(student));
        
    }

    @Test
    public void testCreateSubscription_FreePlanNotFound() {

        // Arrange
        Student student = new Student();
        student.setId(1L);

        when(subscriptionRepository.findByStudent(student)).thenReturn(Optional.empty());

        String planType = "Gratis";

        when(planRepository.findByType(planType)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> subscriptionService.createSubscription(student));

        // Verify
        verify(subscriptionRepository, times(1)).findByStudent(student);
    }
}
