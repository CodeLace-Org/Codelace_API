package com.codelace.codelace.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.ProgressResponseDTO;
import com.codelace.codelace.model.entity.Progress;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProgressMapper {
    private final ModelMapper modelMapper;
	public ProgressResponseDTO convertProgressToResponseDTO(Progress progress){
        return modelMapper.map(progress, ProgressResponseDTO.class);
    }
}
