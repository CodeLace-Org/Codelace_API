package com.codelace.codelace.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.PostMapper;
import com.codelace.codelace.mapper.StudentMapper;
import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostByStudentResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.dto.PostResponseIdDTO;
import com.codelace.codelace.model.dto.StudentResponseDTO;
import com.codelace.codelace.model.entity.Comment;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Rocket;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.CommentRepository;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RocketRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
	private PostRepository postRepository;
	private ProjectRepository projectRepository;
	private StudentRepository studentRepository;
	private RocketRepository rocketRepository;
	private CommentRepository commentRepository;
	private PostMapper postMapper;
	private StudentMapper studentMapper;
	
	// Method that returns a post by its id
	public PostResponseIdDTO getPostById(Long postId) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
		Project project = projectRepository.findById(post.getProject().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + post.getProject().getId()));
		
		List<Rocket> rockets = rocketRepository.findAllByPost(post);
		List<Comment> comments = commentRepository.findAllByPost(post);

		PostResponseIdDTO postResponse = postMapper.convertToResponseIdDTO(post);
		postResponse.setRockets((long) rockets.size());
		postResponse.setComments((long) comments.size());
		postResponse.setTitle(project.getTitle());
		return postResponse;
	}

	// Method that returns all the posts
	public List<PostByProjectResponseDTO> getAllPosts() {
		List<Object[]> posts = postRepository.findAllPosts();

		return posts.stream()
				.map(postMapper::convertToListProjectDTO)
				.toList();
	}

	// Method that creates a post
	public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
		Project project = projectRepository.findById(postRequestDTO.getProject())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + postRequestDTO.getProject()));
		Student student = studentRepository.findById(postRequestDTO.getStudent())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + postRequestDTO.getStudent()));

		Post post = postMapper.convertToEntity(postRequestDTO);
		post.setProject(project);
		post.setStudent(student);
		post.setDate(LocalDate.now());
		post = postRepository.save(post);
		return postMapper.convertToDTO(post);
	}

	// Method that returns a post by student id and project id
	public PostByStudentResponseDTO getPostByStudentAndProject(Long studentId, Long projectId){
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id"));
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with id"));
				
		Post post = postRepository.findByStudentIdAndProjectId(studentId, projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with StudentId and ProjectId"));
				
		StudentResponseDTO studentResponse = studentMapper.convertStudentToResponse(student);
		PostByStudentResponseDTO postResponse = postMapper.convertToResponse(post);
		postResponse.setStudent(studentResponse);
		postResponse.setTitle(project.getTitle());
		return postResponse;
	}

	// Method that returns all the posts by project id
	public List<PostByProjectResponseDTO> getAllPostsByProjectId(Long projectId) {
		List<Object[]> posts = postRepository.findAllByProjectId(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Posts not found for project with id " + projectId));

		return posts.stream()
				.map(postMapper::convertToListProjectDTO)
				.toList();
	}

	// Method that returns a list of post by student id
	public List<PostByStudentResponseDTO> getAllPostsByStudent(Long studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + studentId));
		List<Object[]> posts = postRepository.findAllByStudent(student)
				.orElseThrow(() -> new ResourceNotFoundException("Posts not found for student with id " + studentId));
		
		return posts.stream()
				.map(postMapper::convertToListPostDTO)
				.toList();
	}
}
