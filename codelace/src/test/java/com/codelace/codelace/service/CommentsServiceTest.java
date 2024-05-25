package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.mapper.CommentMapper;
import com.codelace.codelace.model.dto.CommentRequestDTO;
import com.codelace.codelace.model.dto.CommentResponseDTO;
import com.codelace.codelace.model.entity.Comment;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.CommentRepository;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class CommentsServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Mock
	private CommentMapper commentMapper;

	@Mock
	private CommentRepository commentRepository;

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private PostRepository postRepository;

	@Test
	public void testCreateComment() {
		// Arrange
		Long idStudent = 1L;
		Long idPost = 2L;

		CommentRequestDTO requestDTO = new CommentRequestDTO();
		requestDTO.setStudent(idStudent);
		requestDTO.setPost(idPost);
		requestDTO.setStudent(idStudent);
		requestDTO.setContent("Comment 1");

		Student student = new Student();
		student.setId(idStudent);
		student.setUsername("Student 1");
		Post post = new Post();
		post.setId(idPost);
		post.setDescription("Post 1");

		Comment comment = new Comment();
		comment.setId(1L);
		comment.setContent(requestDTO.getContent());
		comment.setDate(LocalDate.now());
		comment.setStudent(student);
		comment.setPost(post);

		// Mock repository
		when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
		when(postRepository.findById(requestDTO.getPost())).thenReturn(Optional.of(post));
		when(commentRepository.save(any(Comment.class))).thenReturn(comment);

		// Mock mapper
		CommentResponseDTO expected = new CommentResponseDTO();
		expected.setContent("Comment 1");
		expected.setDate(LocalDate.now());
		expected.setStudent(student);

		when(commentMapper.convertToResponse(comment)).thenReturn(expected);
		when(commentMapper.convertToEntity(requestDTO)).thenReturn(comment);
		// Act
		CommentResponseDTO response = commentService.createComment(requestDTO);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// verify
		verify(studentRepository, times(1)).findById(idStudent);
		verify(postRepository, times(1)).findById(idPost);
		verify(commentRepository, times(1)).save(any(Comment.class));
		verify(commentMapper, times(1)).convertToResponse(comment);
	}

	@Test
	public void testCreateComment_StudentNotFound() {
		// Arrange
		Long idStudent = 1L;
		Long idPost = 2L;

		CommentRequestDTO requestDTO = new CommentRequestDTO();
		requestDTO.setStudent(idStudent);
		requestDTO.setPost(idPost);
		requestDTO.setStudent(idStudent);
		requestDTO.setContent("Comment 1");

		// Mock repository
		when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.empty());

		// Act
		assertThrows(Exception.class, () -> commentService.createComment(requestDTO));

		// verify
		verify(studentRepository, times(1)).findById(idStudent);
		verify(postRepository, times(0)).findById(idPost);
		verify(commentRepository, times(0)).save(any(Comment.class));
		verify(commentMapper, times(0)).convertToResponse(any(Comment.class));
	}

	@Test
	public void testCreateComment_PostNotFound() {
		// Arrange
		Long idStudent = 1L;
		Long idPost = 2L;

		CommentRequestDTO requestDTO = new CommentRequestDTO();
		requestDTO.setStudent(idStudent);
		requestDTO.setPost(idPost);
		requestDTO.setStudent(idStudent);
		requestDTO.setContent("Comment 1");

		Student student = new Student();
		student.setId(idStudent);
		student.setUsername("Student 1");

		// Mock repository
		when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
		when(postRepository.findById(requestDTO.getPost())).thenReturn(Optional.empty());

		// Act
		assertThrows(Exception.class, () -> commentService.createComment(requestDTO));

		// verify
		verify(studentRepository, times(1)).findById(idStudent);
		verify(postRepository, times(1)).findById(idPost);
		verify(commentRepository, times(0)).save(any(Comment.class));
		verify(commentMapper, times(0)).convertToResponse(any(Comment.class));
	}
}
