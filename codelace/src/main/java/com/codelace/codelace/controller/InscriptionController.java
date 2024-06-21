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

import com.codelace.codelace.model.dto.InscriptionRequestDTO;
import com.codelace.codelace.model.dto.InscriptionResponseDTO;
import com.codelace.codelace.service.InscriptionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/inscriptions")
@AllArgsConstructor
public class InscriptionController {
	private final InscriptionService inscriptionService;

    // Method that returns all the Inscription
    @GetMapping
    public ResponseEntity<List<InscriptionResponseDTO>> getAllInscriptions(){
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getAllInscriptions();
        return new ResponseEntity<>(inscriptions, HttpStatus.OK);
    }

    // Method that returns a inscription by its id
    @GetMapping("{id}")
    public ResponseEntity<InscriptionResponseDTO> getInscriptionById(@PathVariable Long id){
        InscriptionResponseDTO inscription = inscriptionService.getInscriptionById(id);
        return new ResponseEntity<>(inscription, HttpStatus.OK);
    }

    // Method that returns the student's inscriptions
    @GetMapping("students/{id}")
    public ResponseEntity<List<InscriptionResponseDTO>> getAllInscriptionsByStudent(@PathVariable Long id) {
        List<InscriptionResponseDTO> inscriptions = inscriptionService.getAllByStudent(id);
        return new ResponseEntity<>(inscriptions, HttpStatus.OK);
    }

    // Method that creates a Inscription
    @PostMapping
    public ResponseEntity<InscriptionResponseDTO> createInscription(@Validated @RequestBody InscriptionRequestDTO inscriptionRequestDTO) {
        InscriptionResponseDTO inscription = inscriptionService.createInscription(inscriptionRequestDTO);
        return new ResponseEntity<>(inscription, HttpStatus.CREATED);
    }

    // Method that deletes a Inscription
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id){
        inscriptionService.deleteInscription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
