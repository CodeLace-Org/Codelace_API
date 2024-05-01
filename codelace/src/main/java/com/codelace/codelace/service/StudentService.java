package com.codelace.codelace.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.BadRequestException;
import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.StudentMapper;
import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    // Method that returns all the students
    public List<StudentResponseDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.convertToListDTO(students);
    }

    // Method that returns a student by its id
    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found."));
        return studentMapper.convertStudentToResponse(student);
    }

    // Method that creates a student
    public StudentResponseDTO createStudent(StudentRegisterRequestDTO studentRegisterRequestDTO) {

        // Retrieving the information from the request
        String email = studentRegisterRequestDTO.getEmail();
        String username = studentRegisterRequestDTO.getUsername();
        String password = studentRegisterRequestDTO.getPwd();
        String confirmPassword = studentRegisterRequestDTO.getConfirmPassword();

        // Validation: checking if the email is already being used, the username is being used or the passwords are different
        if (studentRepository.findByEmail(email).isPresent()) throw new ResourceDuplicateException("The email address is already in use.");
        if (studentRepository.findByUsername(username).isPresent()) throw new ResourceDuplicateException("The username is already in use.");
        if (!password.equals(confirmPassword)) throw new BadRequestException("Passwords do not match.");

        // Creating the student and returning its information
        Student student = studentMapper.convertStudentRegisterToEntity(studentRegisterRequestDTO);
        student = studentRepository.save(student);
        return studentMapper.convertStudentToResponse(student);

    }

    // Method that updates a student / edit profile (TODO)

    // Method that deletes a student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found."));
        studentRepository.delete(student);
    }
}
