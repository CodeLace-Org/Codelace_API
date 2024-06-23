package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.RocketRequestDTO;
import com.codelace.codelace.model.dto.RocketResponseDTO;
import com.codelace.codelace.service.RocketService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/rockets")
@AllArgsConstructor
public class RocketController {
    // Instance of service rocket
    private final RocketService rocketService;

    // Method that returns all the rockets
    @GetMapping
    public ResponseEntity<List<RocketResponseDTO>> getAllRockets() {
        List<RocketResponseDTO> rockets = rocketService.getAllRockets();
        return new ResponseEntity<>(rockets, HttpStatus.OK);
    }

    // Method that creates a rocket
    @PostMapping
    public ResponseEntity<RocketResponseDTO> createRocket(@RequestBody RocketRequestDTO rocketRequestDTO) {
        RocketResponseDTO response = rocketService.createRocket(rocketRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Method that returns a rocket by its id
    @GetMapping("/posts/{post_id}/students/{student_id}")
    public ResponseEntity<RocketResponseDTO> getRocketByPostIdAndStudentId(@PathVariable Long post_id, @PathVariable Long student_id) {
        RocketResponseDTO rocket = rocketService.getRocketByPostIdAndStudentId(post_id, student_id);
        return new ResponseEntity<>(rocket, HttpStatus.OK);
    }

    // Method that deletes a rocket
    @DeleteMapping("/posts/{post_id}/students/{student_id}")
    public ResponseEntity<RocketResponseDTO> deleteRocketByPostIdAndStudentId(@PathVariable Long post_id, @PathVariable Long student_id) {
        RocketResponseDTO rocket = rocketService.deleteRocketByPostIdAndStudentId(post_id, student_id);
        return new ResponseEntity<>(rocket, HttpStatus.OK);
    }

}
