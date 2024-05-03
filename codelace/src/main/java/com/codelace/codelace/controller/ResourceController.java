package com.codelace.codelace.controller;

import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.dto.ResourceResponseDTO;
import com.codelace.codelace.service.ResourceService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@AllArgsConstructor
@RestController
@RequestMapping("/resources")
public class ResourceController {
	private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources(){
        List<ResourceResponseDTO> resources = resourceService.getAllResource();
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResourceResponseDTO> createResource(@Validated @RequestBody ResourceRequestDTO resourceRequestDTO){
        ResourceResponseDTO resource = resourceService.createResource(resourceRequestDTO);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
    
    
}
