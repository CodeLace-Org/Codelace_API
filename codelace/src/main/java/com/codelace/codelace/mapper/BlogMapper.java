package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.BlogRequestDTO;
import com.codelace.codelace.model.dto.BlogResponseDTO;
import com.codelace.codelace.model.entity.Blog;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BlogMapper {
    
    private final ModelMapper modelMapper;

    // Request Blog DTO to Entity
    public Blog convertToEntity(BlogRequestDTO blogRequestDTO) {
        return modelMapper.map(blogRequestDTO, Blog.class);
    }

    // Blog Entity to Response DTO
    public BlogResponseDTO convertToResponse(Blog blog) {
        return modelMapper.map(blog, BlogResponseDTO.class);
    }

    // List Blog Entity to List response DTO
    public List<BlogResponseDTO> convertToResponse(List<Blog> blogs){
        return blogs.stream()
                .map(this::convertToResponse)
                .toList();
    }
    
}
