package com.codelace.codelace.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.mapper.StudentMapper;
import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.entity.Estudiante;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	private final StudentRepository estudianteRepository;
    private final StudentMapper estudianteMapper;

    // Method that returns all the students
    public List<StudentResponseDTO> getAllStudents() {
        List<Estudiante> students = estudianteRepository.findAll();
        return estudianteMapper.convertToListDTO(students);
    }

    // Method that returns a student by its id
    public StudentResponseDTO getStudentById(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Student not found."));
        return estudianteMapper.convertStudentToResponse(estudiante);
    }

    // Method that creates a student
    public StudentResponseDTO createStudent(StudentRegisterRequestDTO estudianteRegisterRequestDTO) {
        String email = estudianteRegisterRequestDTO.getEmail();
        String username = estudianteRegisterRequestDTO.getUsername();
        String password = estudianteRegisterRequestDTO.getPwd();
        String confirmPassword = estudianteRegisterRequestDTO.getConfirmPassword();

        if (estudianteRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use.");
        } else if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match.");
        } else if (estudianteRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already in use.");
        }else{
            Estudiante estudiante = estudianteMapper.convertStudentRegisterToEntity(estudianteRegisterRequestDTO);
            estudianteRepository.save(estudiante);
            return estudianteMapper.convertStudentToResponse(estudiante);
        }
    }

    // Method that updates a student / edit profile (TODO)

    // Method that deletes a student
    public void deleteStudent(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Student not found."));
        estudianteRepository.delete(estudiante);
    }
}
