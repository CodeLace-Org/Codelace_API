package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.RocketRequestDTO;
import com.codelace.codelace.model.dto.RocketResponseDTO;
import com.codelace.codelace.model.entity.Rocket;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RocketMapper {
    private final ModelMapper modelMapper;

    // Request Rocket DTO to Entity
    public Rocket convertToEntity(RocketRequestDTO rocketRequestDTO) {
        return modelMapper.map(rocketRequestDTO, Rocket.class);
    }

    // Rocket Entity to Response DTO
    public RocketResponseDTO convertToResponse(Rocket rocket) {
        return modelMapper.map(rocket, RocketResponseDTO.class);
    }

    public List<RocketResponseDTO> convertToResponse(List<Rocket> rockets) {
        return rockets.stream()
                .map(this::convertToResponse)
                .toList();
    }
}
