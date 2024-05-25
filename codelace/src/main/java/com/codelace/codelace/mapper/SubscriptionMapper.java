package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.model.entity.Subscription;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SubscriptionMapper {
	
    private final ModelMapper modelMapper;

    // Entity to response DTO
    public SubscriptionResponseDTO convertEntityToResponse(Subscription subscription) {

        return modelMapper.map(subscription, SubscriptionResponseDTO.class);

    }

    // List Entity to List response DTO
    public List<SubscriptionResponseDTO> convertToListDTO(List<Subscription> subscriptions) {
        
        return subscriptions.stream()
            .map(this::convertEntityToResponse)
            .toList();

    }

}
