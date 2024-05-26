package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.service.SubscriptionService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/subscriptions")
@AllArgsConstructor
public class SubscriptionController {
	private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDTO>> getAllSubscriptions() {
        List<SubscriptionResponseDTO> subscriptions = subscriptionService.getAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDTO> getSubscriptionById(@PathVariable Long id){
        SubscriptionResponseDTO subscription = subscriptionService.getSubscriptionById(id);
		return new ResponseEntity<>(subscription, HttpStatus.OK);
    }
    
}
