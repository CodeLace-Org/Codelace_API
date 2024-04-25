package com.codelace.codelace.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.EstudianteLoginRequestDTO;
import com.codelace.codelace.model.dto.EstudianteRegisterRequestDTO;
import com.codelace.codelace.model.entity.Estudiante;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EstudianteMapper {

    private final ModelMapper modelMapper;
    
    // Request Estudiante Login DTO a Entity
	public Estudiante convertEstudianteLoginToEntity(EstudianteLoginRequestDTO estudianteLoginRequestDTO) {
        return modelMapper.map(estudianteLoginRequestDTO, Estudiante.class);
    }

    // Request Estudiante Register DTO a Entity
    public Estudiante convertEstudianteRegisterToEntity(EstudianteRegisterRequestDTO estudianteRegisterRequestDTO) {
        return modelMapper.map(estudianteRegisterRequestDTO, Estudiante.class);
    }
}
