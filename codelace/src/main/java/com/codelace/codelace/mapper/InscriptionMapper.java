package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.InscriptionRequestDTO;
import com.codelace.codelace.model.dto.InscriptionResponseDTO;
import com.codelace.codelace.model.entity.Inscription;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class InscriptionMapper {
	private final ModelMapper modelMapper;
    
    // Request Inscription DTO to Entity;
    public Inscription convertInscriptionRequestToEntity(InscriptionRequestDTO inscriptionRequestDTO) {
        return modelMapper.map(inscriptionRequestDTO, Inscription.class);
    }

    // Inscription Entity to response DTO
    public InscriptionResponseDTO convertInscriptionToResponse(Inscription inscription) {
        return modelMapper.map(inscription, InscriptionResponseDTO.class);
    }

    // List Inscription Entity to List response DTO
    public List<InscriptionResponseDTO> convertToListDTO(List<Inscription> inscriptions) {
        return inscriptions.stream()
                .map(this::convertInscriptionToResponse)
                .toList();
    }


    
}
