package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.BadRequestException;
import com.codelace.codelace.exception.InsufficientSubscriptionPlan;
import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProgressMapper;
import com.codelace.codelace.mapper.StudentMapper;
import com.codelace.codelace.model.dto.ProgressResponseDTO;
import com.codelace.codelace.model.dto.ProjectDetailsResponseDTO;
import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.codelace.codelace.model.dto.StudentRegisterResponseDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.dto.StudentUpdatePasswordRequestDTO;
import com.codelace.codelace.model.dto.StudentUpdateRequestDTO;
import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.model.entity.Subscription;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.ProgressRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;
import com.codelace.codelace.repository.StudentRepository;
import com.codelace.codelace.repository.SubscriptionRepository;

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
	private final ProgressMapper progressMapper;
	private final SubscriptionRepository subscriptionRepository;

	// Instances of services
	private final SubscriptionService subscriptionService;

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
	public StudentRegisterResponseDTO createStudent(StudentRegisterRequestDTO studentRegisterRequestDTO) {

		// Retrieving the information from the request
		String email = studentRegisterRequestDTO.getEmail();
		String username = studentRegisterRequestDTO.getUsername();
		String password = studentRegisterRequestDTO.getPwd();
		String confirmPassword = studentRegisterRequestDTO.getConfirmPassword();

		// Validation: checking if the email is already being used, the username is
		// being used or the passwords are different
		if (studentRepository.findByEmail(email).isPresent())
			throw new ResourceDuplicateException("The email address is already in use.");
		if (studentRepository.findByUsername(username).isPresent())
			throw new ResourceDuplicateException("The username is already in use.");
		if (!password.equals(confirmPassword))
			throw new BadRequestException("Passwords do not match.");

		// Creating the student and returning its information
		Student student = studentMapper.convertStudentRegisterToEntity(studentRegisterRequestDTO);
		student = studentRepository.save(student);

		SubscriptionResponseDTO subscriptionDTO = subscriptionService.createSubscription(student);

		StudentRegisterResponseDTO responseDTO = studentMapper.convertStudentToRegisterResponseDTO(student);
		responseDTO.setSubscription(subscriptionDTO);

		return responseDTO;

	}

	// Method that updates a student / edit profile
	public StudentResponseDTO updateStudent(Long id, StudentUpdateRequestDTO studentUpdateRequestDTO) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found."));

		String email = studentUpdateRequestDTO.getEmail();
		String username = studentUpdateRequestDTO.getUsername();
		String description = studentUpdateRequestDTO.getDescription();
		String status = studentUpdateRequestDTO.getStatus();

		if (!email.equals(student.getEmail()) && studentRepository.findByEmail(email).isPresent())
			throw new ResourceDuplicateException("The email address is already in use.");
		if (!username.equals(student.getUsername()) && studentRepository.findByUsername(username).isPresent())
			throw new ResourceDuplicateException("The username is already in use.");

		student.setEmail(email);
		student.setUsername(username);
		student.setDescription(description);
		student.setStatus(status);
		studentRepository.save(student);
		return studentMapper.convertStudentToResponse(student);
	}

	// Method that updates a student password
	public StudentResponseDTO updateStudentPassword(Long id, StudentUpdatePasswordRequestDTO studentUpdatePasswordRequestDTO){
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found."));
		String pwd = studentUpdatePasswordRequestDTO.getPwd();
		String current_pwd = student.getPwd();

		if(!pwd.equals(current_pwd))
			throw new BadRequestException("Current passwords do not match.");

		String new_pwd = studentUpdatePasswordRequestDTO.getNewPassword();
		String confirm_pwd = studentUpdatePasswordRequestDTO.getConfirmPassword();
		
		if (!new_pwd.equals(confirm_pwd))
			throw new BadRequestException("New passwords do not match.");

		student.setPwd(new_pwd);
		studentRepository.save(student);
		return studentMapper.convertStudentToResponse(student);
	}

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

		Subscription subscription = subscriptionRepository.findById(student.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Subscription not found."));

		Plan plan = subscription.getPlan();

		if (plan.getType().equals("Gratis") && !project.getLevel().equals(1)) {
			throw new InsufficientSubscriptionPlan();
		}

		List<Requirement> requirements = requirementRepository.findAllByProject(project);

		List<ProgressResponseDTO> progressResponseDTOList =
			requirements.stream()
				.map(requirement -> getStudentProgressByRequirement(student, requirement))
				.toList();

		ProjectDetailsResponseDTO projectDetailsResponseDTO = studentMapper.convertProjectToResponse(project);
		projectDetailsResponseDTO.setProgress(progressResponseDTOList);

		return projectDetailsResponseDTO;
	}
	
	public ProgressResponseDTO getStudentProgressByRequirement(Student student, Requirement requirement){
		Progress progress = progressRepository.findByStudentAndRequirement(student, requirement);
		if (progress == null) {
			progress = new Progress();
			progress.setCompleted(false);
			progress.setStudent(student);
			progress.setRequirement(requirement);
			progress = progressRepository.save(progress);
		}

		ProgressResponseDTO progressResponseDTO = progressMapper.convertProgressToResponseDTO(progress);
		progressResponseDTO.setDescription(requirement.getDescription());

		return progressResponseDTO;
	}
}
