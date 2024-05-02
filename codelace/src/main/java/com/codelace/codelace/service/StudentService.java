package com.codelace.codelace.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.BadRequestException;
import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.StudentMapper;
import com.codelace.codelace.model.dto.ProgressResponseDTO;
import com.codelace.codelace.model.dto.ProjectDetailsResponseDTO;
import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.ProgressRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	// Instance mapper
    private final StudentMapper studentMapper;

	// Instances of repositories
	private final StudentRepository studentRepository;
	private final ProjectRepository projectRepository;
	private final InscriptionRepository inscriptionRepository;
	private final RequirementRepository requirementRepository;
	private final ProgressRepository progressRepository;

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

	// Method that returns a Project by RequestDTO
	public ProjectDetailsResponseDTO getDetailsbyStudentAndProject(Long projectId, Long studentId) {

		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found."));

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));

		Route route = project.getRoute();

		inscriptionRepository.findByStudentAndRoute(student, route)
				.orElseThrow(() -> new ResourceNotFoundException("Inscription not found."));

		List<Requirement> requirements = requirementRepository.findAllByProject(project);

		List<ProgressResponseDTO> progressResponseDTOList = new ArrayList<>();

		for (Requirement requirement : requirements) {
			Progress progress = progressRepository.findByStudentAndRequirement(student, requirement);

			ProgressResponseDTO progressResponseDTO = new ProgressResponseDTO();

			progressResponseDTO.setId(progress.getId());
			progressResponseDTO.setCompleted(progress.getCompleted());
			progressResponseDTO.setDescription(requirement.getDescription());

			progressResponseDTOList.add(progressResponseDTO);
		}

		ProjectDetailsResponseDTO projectDetailsResponseDTO = studentMapper.convertProjectToResponse(project);
		projectDetailsResponseDTO.setProgress(progressResponseDTOList);

		return projectDetailsResponseDTO;
	}
}
