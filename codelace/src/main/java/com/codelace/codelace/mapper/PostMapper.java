package com.codelace.codelace.mapper;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PostMapper {
    
	private final ModelMapper modelMapper;

    public Post convertToEntity(PostRequestDTO postRequestDTO){
        return modelMapper.map(postRequestDTO, Post.class);
    }

    public PostResponseDTO convertToDTO(Post post){
        return modelMapper.map(post, PostResponseDTO.class);
    }

    public List<PostResponseDTO> convertToListDTO(List<Post> posts){
        return posts.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PostByProjectResponseDTO convertToListDTO(Object[] postData){
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
