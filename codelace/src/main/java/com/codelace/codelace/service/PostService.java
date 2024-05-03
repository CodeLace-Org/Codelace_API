package com.codelace.codelace.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.PostMapper;
import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
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

    // Method that returns all the posts
    public List<PostResponseDTO> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        return postMapper.convertToListDTO(posts);
    }

    //Method that creates a post
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO){
        Post post = postMapper.convertToEntity(postRequestDTO);
        Project project = projectRepository.findById(postRequestDTO.getProject())
            .orElseThrow(()->new ResourceNotFoundException("Project not found with id " + postRequestDTO.getProject()));
        Student student = studentRepository.findById(postRequestDTO.getStudent())
            .orElseThrow(()->new ResourceNotFoundException("Student not found with id " + postRequestDTO.getStudent()));

        post.setProject(project);
        post.setStudent(student);
        post.setDate(LocalDate.now());
        postRepository.save(post);
        return postMapper.convertToDTO(post);
    }
    
    // Method that returns all the posts by project id
	public List<PostByProjectResponseDTO> getPostsByProjectId(Long id){
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found with id " + id));
        List<Post> posts = postRepository.findAllByProject(project)
            .orElseThrow(() -> new ResourceNotFoundException("Posts not found for project with id " + id));
        
        List<PostByProjectResponseDTO> postByProjectResponseDTOList = new ArrayList<>();
        Long aux = (long) 1;
        if(!posts.isEmpty()){
            Student student = studentRepository.findById(posts.get(0).getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + posts.get(0).getStudent().getId()));

            for (Post post : posts) {
                PostByProjectResponseDTO postByProjectResponseDTO = new PostByProjectResponseDTO();
                List<Rocket> rockets = rocketRepository.findAllByPostAndStudent(post, student)
                        .orElseThrow(() -> new ResourceNotFoundException("Rockets not found for post with id " + id));
                List<Comment> comments = commentRepository.findAllByPostAndStudent(post, student)
                        .orElseThrow(() -> new ResourceNotFoundException("Comments not found for post with id " + id));

                postByProjectResponseDTO.setId(aux);
                postByProjectResponseDTO.setStudent(student);
                postByProjectResponseDTO.setRockets((long) rockets.size());
                postByProjectResponseDTO.setDate(LocalDate.now());
                postByProjectResponseDTO.setImage(post.getImage());
                postByProjectResponseDTO.setComments((long) comments.size());
                postByProjectResponseDTOList.add(postByProjectResponseDTO);
                aux += 1;
            }
        }   
        return postByProjectResponseDTOList;
    }
}
