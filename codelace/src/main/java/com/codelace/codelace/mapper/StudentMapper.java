package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.ProjectDetailsResponseDTO;
import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentRegisterResponseDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.dto.StudentUpdateRequestDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Student;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StudentMapper {

    private final ModelMapper modelMapper;

    // Request Estudiante Register DTO a Entity
    public Student convertStudentRegisterToEntity(StudentRegisterRequestDTO studentRegisterRequestDTO) {
        return modelMapper.map(studentRegisterRequestDTO, Student.class);
    }

    // Request Estudiante Update DTO a Entity
    public Student convertStudentUpdateToEntity(StudentUpdateRequestDTO studentUpdateRequestDTO){
        return modelMapper.map(studentUpdateRequestDTO, Student.class);
    }

    // Estudiante Entity to response DTO
    public StudentResponseDTO convertStudentToResponse(Student student) {
        return modelMapper.map(student, StudentResponseDTO.class);
    }

    // List Estudiante Entity to List response DTO
    public List<StudentResponseDTO> convertToListDTO(List<Student> students) {
        return students.stream()
                .map(this::convertStudentToResponse)
                .toList();
    }

	// Project Entity to Response DTO
    public ProjectDetailsResponseDTO convertProjectToResponse(Project project) {
        return modelMapper.map(project, ProjectDetailsResponseDTO.class);
    }

    // Student Entity to Register Response DTO
    public StudentRegisterResponseDTO convertStudentToRegisterResponseDTO(Student student){
        return modelMapper.map(student, StudentRegisterResponseDTO.class);
    }
}
