package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
//import com.codelace.codelace.model.dto.StudentUpdatePasswordRequestDTO;
import com.codelace.codelace.model.dto.StudentUpdateRequestDTO;
import com.codelace.codelace.model.dto.SubscriptionResponseDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.dto.StudentUpdatePasswordRequestDTO;
import com.codelace.codelace.model.entity.Inscription;
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

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    
    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private InscriptionRepository inscriptionRepository;

    @Mock
    private RequirementRepository requirementRepository;

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private ProgressMapper progressMapper;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetAllStudents(){

        // Arrange
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        List<Student> students = Arrays.asList(student1, student2);

        // Mocking repository
        when(studentRepository.findAll()).thenReturn(students);

        // Mocking mapper
        StudentResponseDTO responseDTO1 = new StudentResponseDTO();
        StudentResponseDTO responseDTO2 = new StudentResponseDTO();
        responseDTO1.setId(student1.getId());
        responseDTO2.setId(student2.getId());

        List<StudentResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);

        when(studentMapper.convertToListDTO(students)).thenReturn(expectedResponse);

        // Act
        List<StudentResponseDTO> result = studentService.getAllStudents();

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());

        // Verify that repository and mapper methods were called
        verify(studentRepository, times(1)).findAll();
        verify(studentMapper, times(1)).convertToListDTO(students);

    }

    @Test
    public void testGetStudentById_ExistingId() {

        // Arrange
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(student.getId());
        when(studentMapper.convertStudentToResponse(student)).thenReturn(responseDTO);

        // Act
        StudentResponseDTO result = studentService.getStudentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        
        // Verify
        verify(studentRepository, times(1)).findById(id);
        verify(studentMapper, times(1)).convertStudentToResponse(student);

    }

    @Test
    public void testGetStudentById_StudentNotFound() {

        // Arrange
        Long id = 999L;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(id));
    
    }

    @Test
    public void testCreateStudent() {

        // Arrange
        StudentRegisterRequestDTO requestDTO = new StudentRegisterRequestDTO();
        requestDTO.setEmail("pepito123@gmail.com");
        requestDTO.setUsername("pepito123");
        requestDTO.setPwd("Hola123456!");
        requestDTO.setConfirmPassword("Hola123456!");

        when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.empty());
        
        Student student = new Student();
        student.setEmail(requestDTO.getEmail());
        student.setUsername(requestDTO.getUsername());
        student.setPwd(requestDTO.getPwd());

        when(studentMapper.convertStudentRegisterToEntity(requestDTO)).thenReturn(student);

        SubscriptionResponseDTO subscription = new SubscriptionResponseDTO();
        subscription.setId(1L);
        subscription.setActive(true);
        subscription.setBeginDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1));
        subscription.setPlanId(1L);
        subscription.setPlanType("Gratis");

        StudentRegisterResponseDTO responseDTO = new StudentRegisterResponseDTO();
        responseDTO.setId(student.getId());
        responseDTO.setUsername(student.getUsername());
        responseDTO.setSubscription(subscription);

        when(subscriptionService.createSubscription(student)).thenReturn(subscription);
        when(studentMapper.convertStudentToRegisterResponseDTO(student)).thenReturn(responseDTO);
        when(studentRepository.save(student)).thenReturn(student);

        // Act
        StudentRegisterResponseDTO result = studentService.createStudent(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(student.getId(), result.getId());
        
        // Verify
        verify(studentRepository, times(1)).findByEmail(requestDTO.getEmail());
        verify(studentRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(studentMapper, times(1)).convertStudentRegisterToEntity(requestDTO);
        verify(studentRepository, times(1)).save(student);
        verify(studentMapper, times(1)).convertStudentToRegisterResponseDTO(student);

    }

    @Test
    public void testCreateStudent_EmailInUse() {

        // Arrange
        StudentRegisterRequestDTO requestDTO = new StudentRegisterRequestDTO();
        requestDTO.setEmail("pepito123@gmail.com");
        requestDTO.setUsername("pepito123");
        requestDTO.setPwd("Hola123456!");
        requestDTO.setConfirmPassword("Hola123456!");

        Student student = new Student();
        student.setId(1L);
        student.setEmail("pepito123@gmail.com");
        student.setUsername("pepe123");

        when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(student));

        // Act & Assert
        assertThrows(ResourceDuplicateException.class, () -> studentService.createStudent(requestDTO));

        // Verify
        verify(studentRepository, times(1)).findByEmail(requestDTO.getEmail());

    }

    @Test
    public void testCreateStudent_UsernameInUse() {

        // Arrange
        StudentRegisterRequestDTO requestDTO = new StudentRegisterRequestDTO();
        requestDTO.setEmail("pepito123@gmail.com");
        requestDTO.setUsername("pepito123");
        requestDTO.setPwd("Hola123456!");
        requestDTO.setConfirmPassword("Hola123456!");

        Student student = new Student();
        student.setId(1L);
        student.setEmail("pepe123@gmail.com");
        student.setUsername("pepito123");

        when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.of(student));

        // Act & Assert
        assertThrows(ResourceDuplicateException.class, () -> studentService.createStudent(requestDTO));

        // Verify
        verify(studentRepository, times(1)).findByEmail(requestDTO.getEmail());
        verify(studentRepository, times(1)).findByUsername(requestDTO.getUsername());
    
    }

    @Test
    public void testCreateStudent_NotMatchingPasswords() {

        // Arrange
        StudentRegisterRequestDTO requestDTO = new StudentRegisterRequestDTO();
        requestDTO.setEmail("pepito123@gmail.com");
        requestDTO.setUsername("pepito123");
        requestDTO.setPwd("Hola123456!");
        requestDTO.setConfirmPassword("Hola12345!");
        
        // Act & Assert
        assertThrows(BadRequestException.class, () -> studentService.createStudent(requestDTO));

        // Verify
        verify(studentRepository, times(1)).findByEmail(requestDTO.getEmail());
        verify(studentRepository, times(1)).findByUsername(requestDTO.getUsername());
    
    }

    @Test
    public void testUpdateStudent_Successfull(){
                
        // Arrange
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setEmail("pepito123@gmail.com");
        student.setUsername("pepito123");
        student.setDescription("Mi nombre es pepito123 y soy un estudiante de programación.");
        student.setStatus("Me llamo pepito123");

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequestDTO requestDTO = new StudentUpdateRequestDTO();
        requestDTO.setEmail("pepito360@gmail.com");
        requestDTO.setUsername("pepito360");
        requestDTO.setDescription("Mi nombre es pepito y soy un estudiante de programación.");
        requestDTO.setStatus("Me llamo pepito");

        when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.empty());

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setEmail(requestDTO.getEmail());
        updatedStudent.setUsername(requestDTO.getUsername());
        updatedStudent.setDescription(requestDTO.getDescription());
        updatedStudent.setStatus(requestDTO.getStatus());

        when(studentRepository.save(updatedStudent)).thenReturn(updatedStudent);
        
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(updatedStudent.getId());
        responseDTO.setUsername(updatedStudent.getUsername());
        responseDTO.setDescription(updatedStudent.getDescription());
        responseDTO.setStatus(updatedStudent.getStatus());

        when(studentMapper.convertStudentToResponse(updatedStudent)).thenReturn(responseDTO);

        // Act
        StudentResponseDTO result = studentService.updateStudent(id, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedStudent.getId(), result.getId());

        // Verify
        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, times(1)).findByEmail(requestDTO.getEmail());
        verify(studentRepository, times(1)).findByUsername(requestDTO.getUsername());
        verify(studentRepository, times(1)).save(updatedStudent);
        verify(studentMapper, times(1)).convertStudentToResponse(updatedStudent);
        
    }

    @Test
    public void testUpdateStudent_StudentNotFound(){
            
            // Arrange
            Long id = 1L;
    
            when(studentRepository.findById(id)).thenReturn(Optional.empty());
    
            StudentUpdateRequestDTO requestDTO = new StudentUpdateRequestDTO();

            // Assert
            assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudent(id, requestDTO));

            // Verify
            verify(studentRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateStudent_EmailInUse(){
    
            // Arrange
            Long id = 1L;
            Student student = new Student();
            student.setId(id);
            student.setUsername("pablito123");
            student.setEmail("pablito123@gmail.com");
            student.setDescription("Mi nombre es pablito123 y soy un estudiante de programación.");
            student.setStatus("Me llamo pablito123");

            when(studentRepository.findById(id)).thenReturn(Optional.of(student));

            Student student_exist = new Student();
            student_exist.setEmail("paquito360@gmail.com");
            student_exist.setUsername("paquito360");
            student_exist.setDescription("Mi nombre es paquito360 y soy un estudiante de programación.");
            student_exist.setStatus("Me llamo paquito360");
   
            StudentUpdateRequestDTO requestDTO = new StudentUpdateRequestDTO();
            requestDTO.setUsername(student.getUsername());
            requestDTO.setEmail("paquito360@gmail.com");
            requestDTO.setDescription(student.getDescription());
            requestDTO.setStatus("Me llamo pablito360");

            when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(student_exist));

            // Act & Assert
            assertThrows(ResourceDuplicateException.class, () -> studentService.updateStudent(id, requestDTO));

            // Verify
            verify(studentRepository, times(1)).findById(id);
            verify(studentRepository, times(1)).findByEmail(requestDTO.getEmail());

    }

    @Test
    public void testUpdateStudent_UsernameInUse(){
    
            // Arrange
            Long id = 1L;
            Student student = new Student();
            student.setId(id);
            student.setUsername("pablito123");
            student.setEmail("pablito123@gmail.com");
            student.setDescription("Mi nombre es pablito123 y soy un estudiante de programación.");
            student.setStatus("Me llamo pablito123");

            when(studentRepository.findById(id)).thenReturn(Optional.of(student));

            Student student_exist = new Student();
            student_exist.setUsername("paquitogod360");
            student_exist.setEmail("paquito720@gmail.com");
            student_exist.setDescription("Mi nombre es paquitogod360 y soy un estudiante de programación.");
            student_exist.setStatus("Me llamo paquitogod360");

            StudentUpdateRequestDTO requestDTO = new StudentUpdateRequestDTO();
            requestDTO.setUsername("paquitogod360");
            requestDTO.setEmail(student.getEmail());
            requestDTO.setDescription(student.getDescription());
            requestDTO.setStatus(student.getStatus());

            when(studentRepository.findByUsername(requestDTO.getUsername())).thenReturn(Optional.of(student_exist));

            // Act & Assert
            assertThrows(ResourceDuplicateException.class, () -> studentService.updateStudent(id, requestDTO));

            // Verify
            verify(studentRepository, times(1)).findById(id);
            verify(studentRepository, times(1)).findByUsername(requestDTO.getUsername());
    }

    @Test
    public void testUpdateStudent_EmailAndUserEquals(){
        
        // Arrange
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setUsername("pablito123");
        student.setEmail("pablito123@gmail.com");
        student.setDescription("Mi nombre es pablito123 y soy un estudiante de programación.");
        student.setStatus("Me llamo pablito123");

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        StudentUpdateRequestDTO requestDTO = new StudentUpdateRequestDTO();
        requestDTO.setUsername(student.getUsername());
        requestDTO.setEmail(student.getEmail());
        requestDTO.setDescription("Me llamo pablito 123");
        requestDTO.setStatus("Me llamo pablito123");

        Student updatedStudent = new Student();
        updatedStudent.setId(id);
        updatedStudent.setUsername(requestDTO.getUsername());
        updatedStudent.setEmail(requestDTO.getEmail());
        updatedStudent.setDescription(requestDTO.getDescription());
        updatedStudent.setStatus(requestDTO.getStatus());

        when(studentRepository.save(updatedStudent)).thenReturn(updatedStudent);

        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setId(updatedStudent.getId());
        responseDTO.setUsername(updatedStudent.getUsername());
        responseDTO.setDescription(updatedStudent.getDescription());
        responseDTO.setStatus(updatedStudent.getStatus());

        when(studentMapper.convertStudentToResponse(updatedStudent)).thenReturn(responseDTO);

        // Act
        StudentResponseDTO result = studentService.updateStudent(id, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedStudent.getId(), result.getId());

        // Verify
        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, times(1)).save(updatedStudent);
        verify(studentMapper, times(1)).convertStudentToResponse(updatedStudent);

    }

    @Test
    public void testUpdateStudentPassword_Successfull(){
            
            // Arrange
            Long id = 1L;
            Student student = new Student();
            student.setId(id);
            student.setUsername("pablito123");
            student.setEmail("pablito123@gmail.com");
            student.setPwd("pablitopassword123");
                
            when(studentRepository.findById(id)).thenReturn(Optional.of(student));

            StudentUpdatePasswordRequestDTO requestDTO = new StudentUpdatePasswordRequestDTO();
            requestDTO.setPwd("pablitopassword123");
            requestDTO.setNewPassword("pablitopassword1234");
            requestDTO.setConfirmPassword("pablitopassword1234");

            when(studentRepository.save(student)).thenReturn(student);

            // Act
            assertDoesNotThrow(() -> studentService.updateStudentPassword(id, requestDTO));

            // Verify
            verify(studentRepository, times(1)).findById(id);
            verify(studentRepository, times(1)).save(student);

    }

    @Test
    public void testUpdateStudentPassword_StudentNotFound(){
        
        // Arrange
        Long id = 1L;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        StudentUpdatePasswordRequestDTO requestDTO = new StudentUpdatePasswordRequestDTO();

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.updateStudentPassword(id, requestDTO));

        // Verify
        verify(studentRepository, times(1)).findById(id);

    }

    @Test
    public void testUpdateStudentPassword_NotMatchingPasswords(){
            
        // Arrange
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setUsername("pablito123");
        student.setEmail("pablito123@gmail.com");
        student.setPwd("pablitopassword123");
                
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        StudentUpdatePasswordRequestDTO requestDTO = new StudentUpdatePasswordRequestDTO();
        requestDTO.setPwd("pablito1234");
        

        // Assert
        assertThrows(BadRequestException.class, () -> studentService.updateStudentPassword(id, requestDTO));

        // Verify
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateStudentPassword_NotMatchingNewPasswords(){
            
        // Arrange
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setUsername("pablito123");
        student.setEmail("pablito123@gmail.com");
        student.setPwd("pablitopassword123");
                
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        StudentUpdatePasswordRequestDTO requestDTO = new StudentUpdatePasswordRequestDTO();
        requestDTO.setPwd("pablitopassword123");
        requestDTO.setNewPassword("pablitocontraseña123");
        requestDTO.setConfirmPassword("pablitocontraseña1234");

        // Assert
        assertThrows(BadRequestException.class, () -> studentService.updateStudentPassword(id, requestDTO));

        // Verify
        verify(studentRepository, times(1)).findById(id);

    }

    @Test
    public void testDeleteStudent_ExistingId() {
        
        // Assert
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        // Act & Assert
        assertDoesNotThrow(() -> studentService.deleteStudent(id));

        // Verify
        verify(studentRepository, times(1)).delete(student);

    }

    @Test
    public void testDeleteStudent_NonExistingId() {

        // Assert
        Long id = 1L;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteStudent(id));
    
    }

    @Test
    public void testGetDetailsByStudentAndProject_Successfull() {

        // Arrange
        Long projectId = 1L;
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        Route route = new Route();
        route.setId(1L);
        route.setName("routeName");
        route.setDescription("routeDescription");
        route.setIcon("icon");

        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
        Project project = new Project();
        project.setId(projectId);
        project.setRoute(route);
        project.setTitle("projectTitle");
        project.setDescription("projectDescription");
        project.setImage(Base64.getDecoder().decode(base64Image));
        project.setLevel(1);

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setType("Gratis");

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setActive(true);
        subscription.setPlan(plan);
        subscription.setStudent(student);

        Inscription inscription = new Inscription();
        inscription.setId(1L);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.of(inscription));
        when(subscriptionRepository.findById(studentId)).thenReturn(Optional.of(subscription));
        
        Requirement requirement1 = new Requirement();
        requirement1.setId(1L);
        requirement1.setDescription("requirementDescription");
        requirement1.setProject(project);
        Requirement requirement2 = new Requirement();
        requirement2.setId(2L);
        requirement2.setDescription("requirementDescription");
        requirement2.setProject(project);
        List<Requirement> requirements = Arrays.asList(requirement1, requirement2);
        
        when(requirementRepository.findAllByProject(project)).thenReturn(requirements);

        Progress progress1 = new Progress();
        progress1.setId(1L);
        progress1.setStudent(student);
        progress1.setRequirement(requirement1);
        progress1.setCompleted(true);
        Progress progress2 = new Progress();
        progress2.setId(2L);
        progress2.setStudent(student);
        progress2.setRequirement(requirement2);
        progress2.setCompleted(false);

        when(progressRepository.findByStudentAndRequirement(student, requirement1)).thenReturn(progress1);
        when(progressRepository.findByStudentAndRequirement(student, requirement2)).thenReturn(progress2);

        ProgressResponseDTO progressResponseDTO1 = new ProgressResponseDTO();
        progressResponseDTO1.setId(1L);
        progressResponseDTO1.setCompleted(true);
        progressResponseDTO1.setDescription("progressDescription");
        ProgressResponseDTO progressResponseDTO2 = new ProgressResponseDTO();
        progressResponseDTO2.setId(2L);
        progressResponseDTO2.setCompleted(false);
        progressResponseDTO2.setDescription("progressDescription");
        List<ProgressResponseDTO> progressDTOs = Arrays.asList(progressResponseDTO1, progressResponseDTO2);

        when(progressMapper.convertProgressToResponseDTO(progress1)).thenReturn(progressResponseDTO1);
        when(progressMapper.convertProgressToResponseDTO(progress2)).thenReturn(progressResponseDTO2);
        
        ProjectDetailsResponseDTO responseDTO = new ProjectDetailsResponseDTO();
        responseDTO.setId(project.getId());
        responseDTO.setTitle(project.getTitle());
        responseDTO.setProgress(progressDTOs);

        when(studentMapper.convertProjectToResponse(project)).thenReturn(responseDTO);

        // Act
        ProjectDetailsResponseDTO result = studentService.getDetailsbyStudentAndProject(projectId, studentId);

        // Assert
        assertNotNull(result);
        assertEquals(project.getId(), result.getId());
        assertEquals(project.getTitle(), result.getTitle());
        assertEquals(progressDTOs.size(), result.getProgress().size());

        // Verify
        verify(studentRepository, times(1)).findById(studentId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
        verify(subscriptionRepository, times(1)).findById(studentId);
        verify(requirementRepository, times(1)).findAllByProject(project);
        verify(studentMapper, times(1)).convertProjectToResponse(project);

    }

    @Test
    public void testGetDetailsByStudentAndProject_StudentNotFound() {

        // Arrange
        Long projectId = 1L;
        Long studentId = 999L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.getDetailsbyStudentAndProject(projectId, studentId));
    
    }

    @Test
    public void testGetDetailsByStudentAndProject_ProjectNotFound() {

        // Arrange
        Long projectId = 999L;
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.getDetailsbyStudentAndProject(projectId, studentId));
        
        // Verify
        verify(studentRepository, times(1)).findById(studentId);
    
    }

    @Test
    public void testGetDetailsByStudentAndProject_InscriptionNotFound() {
        
        // Assert
        Long projectId = 1L;
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        Route route = new Route();
        route.setId(1L);
        route.setName("routeName");
        route.setDescription("routeDescription");
        route.setIcon("icon");

        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
        Project project = new Project();
        project.setId(projectId);
        project.setRoute(route);
        project.setTitle("projectTitle");
        project.setDescription("projectDescription");
        project.setImage(Base64.getDecoder().decode(base64Image));
        project.setLevel(1);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.getDetailsbyStudentAndProject(projectId, studentId));
        
        // Verify
        verify(studentRepository, times(1)).findById(studentId);
        verify(projectRepository, times(1)).findById(projectId);

    }

    @Test
    public void testGetDetailsByStudentAndProject_SubscriptionNotFound() {

        // Arrange
        Long projectId = 1L;
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        Route route = new Route();
        route.setId(1L);
        route.setName("routeName");
        route.setDescription("routeDescription");
        route.setIcon("icon");

        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
        Project project = new Project();
        project.setId(projectId);
        project.setRoute(route);
        project.setTitle("projectTitle");
        project.setDescription("projectDescription");
        project.setImage(Base64.getDecoder().decode(base64Image));
        project.setLevel(1);

        Inscription inscription = new Inscription();
        inscription.setId(1L);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.of(inscription));
        when(subscriptionRepository.findById(studentId)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> studentService.getDetailsbyStudentAndProject(projectId, studentId));

        // Verify
        verify(studentRepository, times(1)).findById(studentId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
    }

    @Test
    public void testGetDetailsByStudentAndProject_InsufficientSubscriptionPlan() {

        // Arrange
        Long projectId = 1L;
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        Route route = new Route();
        route.setId(1L);
        route.setName("routeName");
        route.setDescription("routeDescription");
        route.setIcon("icon");

        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
        Project project = new Project();
        project.setId(projectId);
        project.setRoute(route);
        project.setTitle("projectTitle");
        project.setDescription("projectDescription");
        project.setImage(Base64.getDecoder().decode(base64Image));
        project.setLevel(2);

        Plan plan = new Plan();
        plan.setId(1L);
        plan.setType("Gratis");

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setActive(true);
        subscription.setPlan(plan);
        subscription.setStudent(student);

        Inscription inscription = new Inscription();
        inscription.setId(1L);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(inscriptionRepository.findByStudentAndRoute(student, route)).thenReturn(Optional.of(inscription));
        when(subscriptionRepository.findById(studentId)).thenReturn(Optional.of(subscription));

        // Assert
        assertThrows(InsufficientSubscriptionPlan.class, () -> studentService.getDetailsbyStudentAndProject(projectId, studentId));
        
        // Verify
        verify(studentRepository, times(1)).findById(studentId);
        verify(projectRepository, times(1)).findById(projectId);
        verify(inscriptionRepository, times(1)).findByStudentAndRoute(student, route);
        verify(subscriptionRepository, times(1)).findById(studentId);
    
    }

    @Test
    public void testGetStudentProgressByRequirement() {
        
        // Arrange
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        Requirement requirement = new Requirement();
        requirement.setId(1L);
        requirement.setDescription("requirementDescription");


        Progress progress = new Progress();
        progress.setId(1L);
        progress.setStudent(student);
        progress.setRequirement(requirement);
        progress.setCompleted(true);

        when(progressRepository.findByStudentAndRequirement(student, requirement)).thenReturn(progress);

        ProgressResponseDTO progressResponseDTO = new ProgressResponseDTO();
        progressResponseDTO.setId(progress.getId());
        progressResponseDTO.setCompleted(progress.getCompleted());
        progressResponseDTO.setDescription(requirement.getDescription());
        when(progressMapper.convertProgressToResponseDTO(progress)).thenReturn(progressResponseDTO);

        // Act
        ProgressResponseDTO result = studentService.getStudentProgressByRequirement(student, requirement);

        // Assert
        assertNotNull(result);
        assertEquals(progress.getId(), result.getId());
        assertEquals("requirementDescription", result.getDescription());

        // Verify
        verify(progressRepository, times(1)).findByStudentAndRequirement(student, requirement);
        verify(progressMapper, times(1)).convertProgressToResponseDTO(progress);

    }

    @Test
    public void testGetStudentProgressByRequirement_ProgressNotInitialized() {

        // Arrange
        Long studentId = 1L;

        Student student = new Student();
        student.setId(studentId);

        Requirement requirement = new Requirement();
        requirement.setId(1L);
        requirement.setDescription("requirementDescription");

        when(progressRepository.findByStudentAndRequirement(student, requirement)).thenReturn(null);

        Progress progress = new Progress();
        progress.setId(1L);
        progress.setStudent(student);
        progress.setRequirement(requirement);
        progress.setCompleted(false);

        when(progressRepository.save(any(Progress.class))).thenReturn(progress);

        ProgressResponseDTO progressResponseDTO = new ProgressResponseDTO();
        progressResponseDTO.setId(progress.getId());
        progressResponseDTO.setCompleted(progress.getCompleted());
        progressResponseDTO.setDescription(requirement.getDescription());
        when(progressMapper.convertProgressToResponseDTO(progress)).thenReturn(progressResponseDTO);

        // Act
        ProgressResponseDTO result = studentService.getStudentProgressByRequirement(student, requirement);
    
        // Assert
        assertNotNull(result);
        assertEquals(progress.getId(), result.getId());
        assertEquals("requirementDescription", result.getDescription());

        // Verify
        verify(progressRepository, times(1)).findByStudentAndRequirement(student, requirement);
        verify(progressMapper, times(1)).convertProgressToResponseDTO(progress);
    
    }
}
