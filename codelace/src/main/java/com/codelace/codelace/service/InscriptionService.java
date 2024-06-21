package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InscriptionService {
	private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;

    private final StudentRepository studentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final RouteRepository routeRepository;

    // Method that returns all the inscriptions
    public List<InscriptionResponseDTO> getAllInscriptions() {
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        return inscriptionMapper.convertToListDTO(inscriptions);
    }

    // Method that returns an inscription by its id
    public InscriptionResponseDTO getInscriptionById(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Inscription not found."));
        return inscriptionMapper.convertInscriptionToResponse(inscription);
    }

    // Method that creates an Inscription
    public InscriptionResponseDTO createInscription(InscriptionRequestDTO inscriptionRequestDTO){

        // Get the student
        Student student = studentRepository.findById(inscriptionRequestDTO.getStudent())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found."));

        // Get the route
        Route route = routeRepository.findById(inscriptionRequestDTO.getRoute())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found."));

        // Validate doesnt exist
        if(inscriptionRepository.findByStudentAndRoute(student, route).isPresent()) throw new ResourceDuplicateException("The user is already inscribed in this route.");
        

        // Get the subscription associated to the Student
        Subscription subscription = subscriptionRepository.findById(student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found."));

        // Verify if the subscription is active
        if(!subscription.getActive()) throw new SubscriptionNotActiveException();
        
        // Get the Plan of the Subscription
        Plan plan = subscription.getPlan();

        // In case the plan is 'Free', the Student can only follow one route. Otherwise, any amount of routes.
        if(plan.getType().equals("Gratis") && inscriptionRepository.findByStudent(student).isPresent()) throw new MaxFreeInscriptionException();

        Inscription inscription = inscriptionMapper.convertInscriptionRequestToEntity(inscriptionRequestDTO);
        inscription.setRoute(route);
        inscription.setStudent(student);
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.convertInscriptionToResponse(inscription);

    }

    // Find all inscriptions by student id
    public List<InscriptionResponseDTO> getAllByStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found."));

        List<Inscription> inscriptions = inscriptionRepository.findAllByStudent(student);
        return inscriptionMapper.convertToListDTO(inscriptions);
    }

    // Method that deletes a inscription
    public void deleteInscription(Long id) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Inscription not found."));
        inscriptionRepository.delete(inscription);
    }

}
