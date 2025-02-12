package com.proyect.proy;


import com.proyect.proy.Book.Book;
import com.proyect.proy.Book.BookService;
import com.proyect.proy.Book.Status;
import com.proyect.proy.User.Role;
import com.proyect.proy.User.User;
import com.proyect.proy.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import org.junit.jupiter.api.BeforeEach;


import org.springframework.http.MediaType;


import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
class JwtApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	private Book book1;
	private Book book2;

	@BeforeEach
	public void setUp() {
		book1 = new Book(1L, "El Principito", "Antoine de Saint-Exupéry", "Ficción", "1234567890", Status.AVAILABLE);
		book2 = new Book(2L, "Cien Años de Soledad", "Gabriel García Márquez", "Realismo Mágico", "0987654321", Status.NOT_AVAILABLE);
	}


	@Test
	public void testGetAllBooks() throws Exception {
		Page<Book> booksPage = new PageImpl<>(Arrays.asList(book1, book2));
		Mockito.when(bookService.getAllBooks(any())).thenReturn(booksPage);

		mockMvc.perform(get("/api/books")
						.param("page", "0")
						.param("size", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].title").value("El Principito"))
				.andExpect(jsonPath("$.content[1].title").value("Cien Años de Soledad"));
	}


	@Test
	public void testGetBookById() throws Exception {
		Mockito.when(bookService.getBookById(1L)).thenReturn(Optional.of(book1));

		mockMvc.perform(get("/api/books/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("El Principito"))
				.andExpect(jsonPath("$.author").value("Antoine de Saint-Exupéry"));
	}


	@Test
	public void testCreateBookWithAdminRole() throws Exception {
		Mockito.when(bookService.createBook(any(Book.class))).thenReturn(book1);

		mockMvc.perform(post("/api/books/create/ADMIN")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(book1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("El Principito"));
	}


	@Test
	public void testCreateBookWithUserRole() throws Exception {
		mockMvc.perform(post("/api/books/create/USER")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(book1)))
				.andExpect(status().isForbidden())
				.andExpect(status().reason("Acceso denegado: solo los administradores pueden crear libros."));
	}


	@Test
	public void testUpdateBook() throws Exception {
		Book updatedBook = new Book(1L, "El Principito (Edición Actualizada)", "Antoine de Saint-Exupéry", "Ficción", "1234567890", Status.AVAILABLE);

		Mockito.when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(updatedBook);

		mockMvc.perform(put("/api/books/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(updatedBook)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("El Principito (Edición Actualizada)"));
	}

	@MockBean
	private UserRepository userRepository; // Replace UserRepository

	private User user1;
	private User user2;

	@BeforeEach
	public void setUpUser() {
		user1 = new User();
		user1.setId(1);
		user1.setFirstname("John");
		user1.setLastname("Doe");
		user1.setUsername("johndoe");
		user1.setCountry("USA");
		user1.setPassword("password");
		user1.setRole(Role.USER);

		user2 = new User();
		user2.setId(2);
		user2.setFirstname("Jane");
		user2.setLastname("Smith");
		user2.setUsername("janesmith");
		user2.setCountry("Canada");
		user2.setPassword("password");
		user2.setRole(Role.ADMIN);


	}

	@Test
	public void testGetAllUsers() throws Exception {
		Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2));

		mockMvc.perform(get("/api/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].username").value("johndoe"))
				.andExpect(jsonPath("$[1].username").value("janesmith"));
	}

	@Test
	public void testGetUserById() throws Exception {
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user1));

		mockMvc.perform(get("/api/users/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("johndoe"))
				.andExpect(jsonPath("$.firstname").value("John"));
	}


	@Test
	public void testCreateUser() throws Exception {
		Mockito.when(userRepository.save(any(User.class))).thenReturn(user1); // Mock the save operation

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(user1)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("johndoe"));
	}

	@Test
	public void testUpdateUser() throws Exception {
		User updatedUser = new User();
		updatedUser.setId(1);
		updatedUser.setFirstname("John Updated");
		updatedUser.setLastname("Doe Updated");
		updatedUser.setUsername("johndoe");
		updatedUser.setCountry("USA");
		updatedUser.setPassword("newPassword");
		updatedUser.setRole(Role.USER);


		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user1));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(updatedUser);

		mockMvc.perform(put("/api/users/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(updatedUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstname").value("John Updated"));
	}



	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}




}
