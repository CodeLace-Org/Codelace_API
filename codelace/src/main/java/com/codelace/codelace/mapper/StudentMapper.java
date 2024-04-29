package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.entity.Estudiante;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StudentMapper {

    private final ModelMapper modelMapper;

    // Request Estudiante Register DTO a Entity
    public Estudiante convertStudentRegisterToEntity(StudentRegisterRequestDTO studentRegisterRequestDTO) {
        return modelMapper.map(studentRegisterRequestDTO, Estudiante.class);
    }

    // Estudiante Entity to response DTO
    public StudentResponseDTO convertStudentToResponse(Estudiante student) {
        return modelMapper.map(student, StudentResponseDTO.class);
    }

    // List Estudiante Entity to List response DTO
    public List<StudentResponseDTO> convertToListDTO(List<Estudiante> estudiantes) {
        return estudiantes.stream()
                .map(this::convertStudentToResponse)
                .toList();
    }
}
