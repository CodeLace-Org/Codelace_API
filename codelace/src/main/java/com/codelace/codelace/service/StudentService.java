package com.codelace.codelace.service;


import java.util.List;

import org.springframework.stereotype.Service;

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
                        () -> new RuntimeException("Student not found."));
        return studentMapper.convertStudentToResponse(student);
    }

    // Method that creates a student
    public StudentResponseDTO createStudent(StudentRegisterRequestDTO studentRegisterRequestDTO) {
        String email = studentRegisterRequestDTO.getEmail();
        String username = studentRegisterRequestDTO.getUsername();
        String password = studentRegisterRequestDTO.getPwd();
        String confirmPassword = studentRegisterRequestDTO.getConfirmPassword();

        if (studentRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use.");
        } else if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match.");
        } else if (studentRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username already in use.");
        }else{
            Student student = studentMapper.convertStudentRegisterToEntity(studentRegisterRequestDTO);
            studentRepository.save(student);
            return studentMapper.convertStudentToResponse(student);
        }
    }

    // Method that updates a student / edit profile (TODO)

    // Method that deletes a student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Student not found."));
        studentRepository.delete(student);
    }
}
