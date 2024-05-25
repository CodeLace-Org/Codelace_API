package com.codelace.codelace.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.SubscriptionMapper;
import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.model.entity.Subscription;
import com.codelace.codelace.repository.PlanRepository;
import com.codelace.codelace.repository.SubscriptionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubscriptionService {
	
    // Instance mapper
    private final SubscriptionMapper subscriptionMapper;

    // Instance repository
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    // Method that returns all the subscriptions
    public List<SubscriptionResponseDTO> getAllSubscriptions() {

        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptionMapper.convertToListDTO(subscriptions);

    }

    // Method that creates a subscription
    public SubscriptionResponseDTO createSubscription(Student student) {

        String type = "Gratis";

        if (subscriptionRepository.findById(student.getId()).isPresent())
            throw new ResourceDuplicateException("Subscription already exists");

        Plan plan = planRepository.findByType(type)
            .orElseThrow(
                () -> new ResourceNotFoundException("Plan not found"));
        
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonthDate = currentDate.plusMonths(1);

        Subscription subscription = new Subscription();
        subscription.setStudent(student);
        subscription.setActive(true);
        subscription.setBeginDate(currentDate);
        subscription.setEndDate(nextMonthDate);
        subscription.setPlan(plan);

        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.convertEntityToResponse(subscription);

    }

}
