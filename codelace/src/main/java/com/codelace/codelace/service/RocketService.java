package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.RocketMapper;
import com.codelace.codelace.model.dto.RocketRequestDTO;
import com.codelace.codelace.model.dto.RocketResponseDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Rocket;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.RocketRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RocketService {
	//Instance mapper
    private final RocketMapper rocketMapper;
    private final RocketRepository rocketRepository;
    private final StudentRepository studentRepository;
    private final PostRepository postRepository;

    // Method that returns all rockets
    public List<RocketResponseDTO> getAllRockets() {
        List<Rocket> rockets = rocketRepository.findAll();
        return rocketMapper.convertToResponse(rockets);
    }

    // Method that creates a rocket
    public RocketResponseDTO createRocket(RocketRequestDTO rocketRequestDTO) {
        Student student = studentRepository
                .findById(rocketRequestDTO.getStudent())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found."));

        Post post = postRepository
                .findById(rocketRequestDTO.getPost())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found."));
                
        Rocket rocket = rocketMapper.convertToEntity(rocketRequestDTO);
        rocket.setPost(post);
        rocket.setStudent(student);
        rocket = rocketRepository.save(rocket);
        return rocketMapper.convertToResponse(rocket);
    }

    // Method to get rockets by post and student
    public RocketResponseDTO getRocketByPostIdAndStudentId(Long post_id, Long student_id) {
        Rocket rocket = rocketRepository.findByPostIdAndStudentId(post_id, student_id)
                .orElseThrow(() -> new ResourceNotFoundException("Rocket not found."));
        
        return rocketMapper.convertToResponse(rocket);
    }

    // Method that deletes a rocket
    public RocketResponseDTO deleteRocketByPostIdAndStudentId(Long postId, Long studentId) {
        Rocket rocket = rocketRepository.findByPostIdAndStudentId(postId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Rocket not found."));
        rocketRepository.delete(rocket);
        return rocketMapper.convertToResponse(rocket);
    }
}
