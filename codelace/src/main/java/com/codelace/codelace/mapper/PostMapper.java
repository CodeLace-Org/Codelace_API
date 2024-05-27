package com.codelace.codelace.mapper;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostByStudentResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.dto.PostResponseIdDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PostMapper {
    
	private final ModelMapper modelMapper;
    private final StudentMapper studentMapper;

    public Post convertToEntity(PostRequestDTO postRequestDTO){
        return modelMapper.map(postRequestDTO, Post.class);
    }

    public PostResponseDTO convertToDTO(Post post){
        return modelMapper.map(post, PostResponseDTO.class);
    }

    public PostResponseIdDTO convertToResponseIdDTO(Post post) {
        return modelMapper.map(post, PostResponseIdDTO.class);
    }

    public List<PostResponseDTO> convertToListDTO(List<Post> posts){
        return posts.stream()
                .map(this::convertToDTO)
                .toList();
    }
    
    public PostByStudentResponseDTO convertToResponse(Post post) {
        return modelMapper.map(post, PostByStudentResponseDTO.class);
    }

    public PostByStudentResponseDTO convertToListPostDTO(Object[] postData){
        PostByStudentResponseDTO postByStudentResponse = new PostByStudentResponseDTO();
        postByStudentResponse.setId((Long) postData[0]);
        postByStudentResponse.setStudent(studentMapper.convertStudentToResponse((Student) postData[1]));
        postByStudentResponse.setTitle((String) postData[2]);
        postByStudentResponse.setDemoUrl((String) postData[3]);
        postByStudentResponse.setRepoUrl((String) postData[4]);
        postByStudentResponse.setDescription((String) postData[5]);
        postByStudentResponse.setDate(((LocalDate) postData[6]));
        postByStudentResponse.setImage((byte[]) postData[7]);
        return postByStudentResponse;
    }

    public PostByProjectResponseDTO convertToListProjectDTO(Object[] postData){
        PostByProjectResponseDTO postByProjectResponse = new PostByProjectResponseDTO();
        postByProjectResponse.setId((Long) postData[0]);
        postByProjectResponse.setStudent((Student) postData[1]);
        postByProjectResponse.setRockets((Long) postData[2]);
        postByProjectResponse.setComments((Long) postData[3]);
        postByProjectResponse.setDate((LocalDate) postData[4]);
        postByProjectResponse.setImage((byte[]) postData[5]);
        return postByProjectResponse;
    }
}
