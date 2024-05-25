package com.codelace.codelace.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
// import com.codelace.codelace.exception.SubscriptionNotActiveException;
import com.codelace.codelace.mapper.SubscriptionMapper;
import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.model.entity.Subscription;
import com.codelace.codelace.repository.PlanRepository;
// import com.codelace.codelace.repository.StudentRepository;
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
    // private final StudentRepository studentRepository;

    // Method that returns all the subscriptions
    public List<SubscriptionResponseDTO> getAllSubscriptions() {

        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptionMapper.convertToListDTO(subscriptions);

    }

    public SubscriptionResponseDTO getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

        return subscriptionMapper.convertEntityToResponse(subscription);
    }

    // Method that creates a subscription
    public SubscriptionResponseDTO createSubscription(Student student) {

        String type = "Gratis";

        if (subscriptionRepository.findByStudent(student).isPresent())
            throw new ResourceDuplicateException("Subscription already exists");

        Plan plan = planRepository.findByType(type)
            .orElseThrow(
                () -> new ResourceNotFoundException("Plan not found"));
        
        LocalDate beginDate = LocalDate.now();
        LocalDate endDate = beginDate.plusMonths(1);

        Subscription subscription = new Subscription();
        subscription.setStudent(student);
        subscription.setActive(true);
        subscription.setBeginDate(beginDate);
        subscription.setEndDate(endDate);
        subscription.setPlan(plan);

        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.convertEntityToResponse(subscription);

    }

    // public SubscriptionResponseDTO verifySubscriptionOutdated(Long studentId){

    //     Student student = studentRepository.findById(studentId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

    //     Subscription subscription = subscriptionRepository.findByStudent(student)
    //             .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

    //     LocalDate currentDate = LocalDate.now();
        
    //     if(currentDate.isAfter(subscription.getEndDate())){
    //         subscription.setActive(false);
    //         subscription = subscriptionRepository.save(subscription);
    //         throw new SubscriptionNotActiveException();
    //     }

    //     return subscriptionMapper.convertEntityToResponse(subscription);
    
    // }

    // public SubscriptionResponseDTO cancelSubscription(Long studentId) {
    //     Student student = studentRepository.findById(studentId)
    //             .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        
    //     Subscription subscription = subscriptionRepository.findByStudent(student)
    //             .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));

    //     Plan plan = planRepository.findByType("Gratis")
    //             .orElseThrow(() -> new ResourceDuplicateException("Plan not found"));

    //     LocalDate beginDate = LocalDate.now();
    //     LocalDate endDate = beginDate.plusMonths(1);

    //     subscription.setPlan(plan);
    //     subscription.setActive(true);
    //     subscription.setBeginDate(beginDate);
    //     subscription.setEndDate(endDate);

    //     subscription = subscriptionRepository.save(subscription);

    //     return subscriptionMapper.convertEntityToResponse(subscription);
        
    // }
}
