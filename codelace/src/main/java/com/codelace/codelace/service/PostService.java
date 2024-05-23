package com.codelace.codelace.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.PostMapper;
import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
	private PostRepository postRepository;
	private ProjectRepository projectRepository;
	private StudentRepository studentRepository;
	private PostMapper postMapper;

	// Method that returns all the posts
	public List<PostResponseDTO> getAllPosts() {
		List<Post> posts = postRepository.findAll();
		return postMapper.convertToListDTO(posts);
	}

	// Method that creates a post
	public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
		Post post = postMapper.convertToEntity(postRequestDTO);
		Project project = projectRepository.findById(postRequestDTO.getProject())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Project not found with id " + postRequestDTO.getProject()));
		Student student = studentRepository.findById(postRequestDTO.getStudent())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Student not found with id " + postRequestDTO.getStudent()));

		post.setProject(project);
		post.setStudent(student);
		post.setDate(LocalDate.now());
		postRepository.save(post);
		return postMapper.convertToDTO(post);
	}

	// Method that returns all the posts by project id
	public List<PostByProjectResponseDTO> getPostsByProjectId(Long id) {
		List<Object[]> posts = postRepository.findAllByProjectId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Posts not found for project with id " + id));

		return posts.stream()
				.map(postMapper::convertToListDTO)
				.toList();
	}

	// Method that returns a list of post by student id
	public List<PostResponseDTO> getPostsByStudent(Long id_student) {
		Student student = studentRepository.findById(id_student)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id_student));
		List<Post> posts = postRepository.findAllByStudent(student)
				.orElseThrow(() -> new ResourceNotFoundException("Posts not found for student with id " + id_student));
		return postMapper.convertToListDTO(posts);
	}
}
