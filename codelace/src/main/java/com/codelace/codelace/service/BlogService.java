package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.BlogMapper;
import com.codelace.codelace.model.dto.BlogRequestDTO;
import com.codelace.codelace.model.dto.BlogResponseDTO;
import com.codelace.codelace.model.entity.Blog;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.repository.BlogRepository;
import com.codelace.codelace.repository.ProjectRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogService {
	// Instance mapper
	private final BlogMapper blogMapper;

	// Instances of repositories
	private final BlogRepository blogRepository;
	private final ProjectRepository projectRepository;

	// Method that returns all the blogs
	public List<BlogResponseDTO> getAllBlogs() {
		List<Blog> blogs = blogRepository.findAll();
		return blogMapper.convertToResponse(blogs);
	}

	// Method that returns a blog by its id
	public BlogResponseDTO getBlogById(Long id) {
		Blog blog = blogRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found."));
		return blogMapper.convertToResponse(blog);
	}

	// Method that creates a student
	public BlogResponseDTO createBlog(BlogRequestDTO blogRequestDTO) {

		// Retrieving the information from the request
		String title = blogRequestDTO.getTitle();

		Project project = projectRepository.findById(blogRequestDTO.getProject())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));
		// Validation: the title must be unique
		if (blogRepository.findByTitle(title).isPresent())
			throw new ResourceNotFoundException("The title is already in use.");

		// Creating the blog
		Blog blog = blogMapper.convertToEntity(blogRequestDTO);
		blog.setProject(project);
		blog = blogRepository.save(blog);
		return blogMapper.convertToResponse(blog);
	}

	// Method that updates a blog
	public BlogResponseDTO updateBlog(Long id, BlogRequestDTO blogRequestDTO) {
		Blog blog = blogRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found."));

		Project project = projectRepository.findById(blogRequestDTO.getProject())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));

		String title = blogRequestDTO.getTitle();
		String content = blogRequestDTO.getContent();
		byte[] img = blogRequestDTO.getImage();

		if (!blog.getTitle().equals(title) && blogRepository.findByTitle(title).isPresent())
			throw new ResourceDuplicateException("The title is already in use.");

		if (img == null)
			img = blog.getImage();

		blog.setContent(content);
		blog.setImage(img);
		blog.setProject(project);
		blogRepository.save(blog);
		return blogMapper.convertToResponse(blog);
	}

	// Method that deletes a blog
	public void deleteBlog(Long id) {
		Blog blog = blogRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Blog not found."));
		blogRepository.delete(blog);
	}

	public List<BlogResponseDTO> getAllByProject(Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found"));
		List<Blog> blogs = blogRepository.findAllByProject(project);
		return blogMapper.convertToResponse(blogs);
	}

}
