package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.PostMapper;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.repository.PostRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    private PostMapper postMapper;

	public List<PostResponseDTO> getPostsByProjectId(Long id){
        List<Post> posts = postRepository.findByProjectId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Posts not found for project with id " + id));
        return postMapper.convertToListDTO(posts);
    }
}
