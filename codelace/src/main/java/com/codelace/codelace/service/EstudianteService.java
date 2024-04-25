package com.codelace.codelace.service;

import org.springframework.stereotype.Service;

import com.codelace.codelace.mapper.EstudianteMapper;
import com.codelace.codelace.model.dto.EstudianteLoginRequestDTO;
import com.codelace.codelace.model.dto.EstudianteRegisterRequestDTO;
import com.codelace.codelace.model.entity.Estudiante;
import com.codelace.codelace.repository.EstudianteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EstudianteService {
	private final EstudianteRepository estudianteRepository;
    private final EstudianteMapper estudianteMapper;

    // Method that creates a student
    public void createStudent(EstudianteRegisterRequestDTO estudianteRegisterRequestDTO) {
        String email = estudianteRegisterRequestDTO.getEmail();
        String username = estudianteRegisterRequestDTO.getUsername();
        String password = estudianteRegisterRequestDTO.getPassword();
        String confirmPassword = estudianteRegisterRequestDTO.getConfirmPassword();

        if (estudianteRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("El correo electrónico está en uso.");
        } else if (password != confirmPassword) {
            throw new RuntimeException("Las contraseñas no coinciden.");
        } else if (estudianteRepository.findByUsuario(username).isPresent()){
            throw new RuntimeException("El usuario está en uso.");
        }else{
            Estudiante estudiante = estudianteMapper.convertEstudianteRegisterToEntity(estudianteRegisterRequestDTO);
            estudianteRepository.save(estudiante);
        }
    }

    //Method that finds a student by email and password
    public Estudiante findStudentByEmailAndPassword(EstudianteLoginRequestDTO estudianteLoginRequestDTO) {
        String email = estudianteLoginRequestDTO.getEmail();
        String password = estudianteLoginRequestDTO.getPassword();
        if(!estudianteRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("No existe ninguna cuenta asociada al correo. Por favor, regístrese.");
        }
        return estudianteRepository.findByEmail(email)
                .filter(estudiante -> estudiante.getPassword().equals(password))
                .orElseThrow(
                        () -> new RuntimeException("Contraseña incorrecta. Por favor, inténtelo de nuevo."));
        
    }

    // Method that updates a student
    public void updateStudent(Long id, EstudianteRegisterRequestDTO estudianteRegisterRequestDTO) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Estudiante no encontrado"));
        estudiante.setEmail(estudianteRegisterRequestDTO.getEmail());
        estudiante.setUsuario(estudianteRegisterRequestDTO.getUsername());
        estudiante.setPassword(estudianteRegisterRequestDTO.getPassword());
        estudianteRepository.save(estudiante);
    }

    // Method that deletes a student
    public void deleteStudent(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Estudiante no encontrado"));
        estudianteRepository.delete(estudiante);
    }

}
