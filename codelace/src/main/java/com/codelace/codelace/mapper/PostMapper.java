package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.entity.Post;

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

    public PostByProjectResponseDTO convertPostToDTO(Post post){
        return modelMapper.map(post, PostByProjectResponseDTO.class);
    }

    public List<PostByProjectResponseDTO> convertPostsToListDTO(List<Post> posts){
        return posts.stream()
                .map(this::convertPostToDTO)
                .toList();
    }
}
