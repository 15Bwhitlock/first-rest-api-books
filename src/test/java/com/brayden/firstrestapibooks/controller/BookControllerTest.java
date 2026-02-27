package com.brayden.firstrestapibooks.controller;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.exception.ApiException;
import com.brayden.firstrestapibooks.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(BookControllerTest.JacksonTestConfig.class)
public class BookControllerTest {

    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // This is needed to explicitly add the bean so @Autowired can inject ObjectMapper.
    @TestConfiguration
    static class JacksonTestConfig {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    private BookRequestDTO bookRequestDTO;
    private BookResponseDTO bookResponseDTO;

    // This method runs before every test.
    @BeforeEach
    void setUp() {
        // This is mock data sent in the request.
        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthor("authorTest");
        bookRequestDTO.setName("nameTest");
        bookRequestDTO.setPrice("priceTest");

        // This is mock data returned in the response.
        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId("1");
        bookResponseDTO.setAuthor("authorTest");
        bookResponseDTO.setName("nameTest");
        bookResponseDTO.setPrice("priceTest");
    }

    // ---- findAllBooks ----

    @Test
    void testFindAllBooks_whenBooksExists_shouldReturnBookList() throws Exception {
        when(bookService.findAllBooks()).thenReturn(List.of(bookResponseDTO));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookResponseDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(bookResponseDTO.getName()))
                .andExpect(jsonPath("$[0].author").value(bookResponseDTO.getAuthor()))
                .andExpect(jsonPath("$[0].price").value(bookResponseDTO.getPrice()));
        verify(bookService).findAllBooks();
    }

    @Test
    void testFindAllBooks_whenNoBooksExist_shouldReturnEmptyList() throws Exception {
        when(bookService.findAllBooks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookService).findAllBooks();
    }

    // ---- createBook ----

    @Test
    void testCreateBook_whenValidRequest_shouldReturnCreatedBook() throws Exception {
        when(bookService.createBook(any(BookRequestDTO.class))).thenReturn(bookResponseDTO);

        mockMvc.perform(post("/books")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookResponseDTO.getId()))
                .andExpect(jsonPath("$.name").value(bookResponseDTO.getName()))
                .andExpect(jsonPath("$.author").value(bookResponseDTO.getAuthor()))
                .andExpect(jsonPath("$.price").value(bookResponseDTO.getPrice()));

        verify(bookService, times(1)).createBook(any(BookRequestDTO.class));
    }

    // ---- updateBook ----

    // When naming tests, use this format.
    // void test[Name of method to be tested]_[Situation of test]_[What test should return]
    @Test
    void testUpdateBook_whenBookExists_shouldReturnUpdatedBook() throws Exception {
        String bookId = "1";

        // The line below mocks updateBook to return a value as if updateBook ran with this response.
        when(bookService.updateBook(bookId, bookRequestDTO)).thenReturn(bookResponseDTO);
        // perform() is used to run the test request.
        // put() specifies the URL path to request.
        // bookId specifies the id to use for the request.
        // contentType is the type of content sent to the endpoint (JSON here).
        // content holds the request object.
        // andExpect checks expected values after the request completes.
        mockMvc.perform(put("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookResponseDTO.getId()))
                .andExpect(jsonPath("$.name").value(bookResponseDTO.getName()))
                .andExpect(jsonPath("$.author").value(bookResponseDTO.getAuthor()))
                .andExpect(jsonPath("$.price").value(bookResponseDTO.getPrice()));
        // times() specifies how many calls are expected.
        // Here the method is called once.
        verify(bookService, times(1)).updateBook(bookId, bookRequestDTO);
    }

    @Test
    void testUpdateBook_whenBookNotFound_shouldReturnApiException() throws Exception {
        String bookId = "1";
        String errorMessage = "Book by that ID not found";

        // thenThrow() is used when an error is expected.
        when(bookService.updateBook(bookId, bookRequestDTO)).thenThrow(new ApiException(errorMessage, HttpStatus.NOT_FOUND));

        // In this error case, we expect an error message in the response.
        mockMvc.perform(put("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));
        // We still expect one call to bookService.updateBook.
        verify(bookService).updateBook(bookId, bookRequestDTO);
    }

    // ---- deleteBook ----

    @Test
    void testDeleteBook_whenBookExists_shouldReturnStatusOk() throws Exception {
        String bookId = "1";

        // Use doNothing() when mocking a void method.
        // Use only when testing controller
        doNothing().when(bookService).deleteBook(bookId);

        // Mocks the service delete call
        // and verifies the endpoint returns 200 OK.
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isOk());

        verify(bookService).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_whenBookNotFound_shouldReturnApiException() throws Exception {
        String bookId = "1";
        // I can choose the error message here.
        String messageException = "ID not found";

        // Use this when mocking a void method and expecting an error.
        doThrow(new ApiException(messageException, HttpStatus.NOT_FOUND)).when(bookService).deleteBook(bookId);

        // Mocks the service delete call
        // and verifies the endpoint fails.
        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(messageException));

        verify(bookService).deleteBook(bookId);
    }

    // ---- findByAuthor ----

    @Test
    void testFindByAuthor_whenBooksExist_shouldReturnBookList() throws Exception {
        String author = bookResponseDTO.getAuthor();

        when(bookService.findByAuthor(author))
                .thenReturn(List.of(bookResponseDTO));

        mockMvc.perform(get("/books/author/{author}", author))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(bookResponseDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(bookResponseDTO.getName()))
                .andExpect(jsonPath("$[0].author").value(bookResponseDTO.getAuthor()))
                .andExpect(jsonPath("$[0].price").value(bookResponseDTO.getPrice()));

        verify(bookService).findByAuthor(author);
    }

    @Test
    void testFindByAuthor_whenNoBooksExist_shouldReturnEmptyList() throws Exception {
        String author = "Nonexistent Author";

        when(bookService.findByAuthor(author)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books/author/{author}", author))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookService).findByAuthor(author);
    }
}
