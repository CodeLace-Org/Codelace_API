package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.ProjectDetailsResponseDTO;
import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentRegisterResponseDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.dto.StudentUpdateRequestDTO;
import com.codelace.codelace.service.StudentService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {
	private final StudentService studentService;

	// ---------------------------- ENDPOINTS (CRUD)
	// Method that returns all the students
	@GetMapping
	public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
		List<StudentResponseDTO> students = studentService.getAllStudents();
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	// Method that returns a student by its id
	@GetMapping("/{id}")
	public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
		StudentResponseDTO student = studentService.getStudentById(id);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	// Method that creates a student
	@PostMapping
	public ResponseEntity<StudentRegisterResponseDTO> createStudent(
			@Validated @RequestBody StudentRegisterRequestDTO studentRegisterRequestDTO) {
		StudentRegisterResponseDTO student = studentService.createStudent(studentRegisterRequestDTO);
		return new ResponseEntity<>(student, HttpStatus.CREATED);
	}

	// Method that updates a student
	@PutMapping("/{id}")
	public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateRequestDTO studentUpdateRequestDTO) {
		// StudentRegisterRequestDTO updatedStudent = studentService.updateStudent(id, studentRegisterRequestDTO);
		StudentResponseDTO updatedStudent = studentService.updateStudent(id, studentUpdateRequestDTO);
		return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
	}

	// Method that deletes a student
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ---------------------------- ENDPOINTS

	@GetMapping("/{id_student}/projects/{id_project}/details")
	public ResponseEntity<ProjectDetailsResponseDTO> getDetailsbyStudentAndProject(@PathVariable long id_student,
			@PathVariable long id_project) {
		ProjectDetailsResponseDTO projectDetailsResponseDTO = studentService.getDetailsbyStudentAndProject(id_project,
				id_student);
		return new ResponseEntity<>(projectDetailsResponseDTO, HttpStatus.OK);
	}

}
