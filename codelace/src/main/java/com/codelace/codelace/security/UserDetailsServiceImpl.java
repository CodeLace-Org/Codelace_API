package com.codelace.codelace.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
        Student student = studentRepository
                    .findByEmail(username)
                    .orElseThrow(
                        () -> new ResourceNotFoundException("Student not found")
                    );
        
        return org.springframework.security.core.userdetails.User
                .withUsername(student.getEmail())
                .password(student.getPwd())
                .roles(student.getRole().name())
                .build();
    }
}
