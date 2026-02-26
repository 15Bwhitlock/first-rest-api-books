package com.brayden.firstrestapibooks.controller;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.exception.ApiException;
import com.brayden.firstrestapibooks.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
public class BookControllerTest {

    @MockitoBean
    private BookService bookService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookRequestDTO bookRequestDTO;
    private BookResponseDTO bookResponseDTO;

    // this method runs before every test
    @BeforeEach
    void setUp() {
        // this is mock data sent in the request
        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthor("authorTest");
        bookRequestDTO.setName("nameTest");
        bookRequestDTO.setPrice("priceTest");

        // this is mock data returned in the response
        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId("1");
        bookResponseDTO.setAuthor("authorTest");
        bookResponseDTO.setName("nameTest");
        bookResponseDTO.setPrice("priceTest");
    }

    // when making a test name use this format
    // void test[Name of method to be tested]_[Situation of test]_[What test should return]
    @Test
    void testUpdateBook_whenBookExists_shouldReturnUpdatedBook() throws Exception {
        String bookId = "1";

        // the line below mocks updateBook to return a value as if updateBook ran with this response
        when(bookService.updateBook(bookId, bookRequestDTO)).thenReturn(bookResponseDTO);
        // the perform() is used to do a test task
        // use put() to specify the url path to request
        // bookId specifies what id to use for the request
        // contentType refers to the type of content that should be sent to the endpoint in this case we want json
        // content holds the object
        // andExpect will have the expected value to test against after the test is over
        // second andExpect checks that the given id is the same as the one expected and so on with the others.
        mockMvc.perform(put("/books/{id}", bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookResponseDTO.getId()))
                .andExpect(jsonPath("$.name").value(bookResponseDTO.getName()))
                .andExpect(jsonPath("$.author").value(bookResponseDTO.getAuthor()))
                .andExpect(jsonPath("$.price").value(bookResponseDTO.getPrice()));
        // times is used to specify how many times we expect this method to be called
        // in this case the method is only called once so it is not needed
        verify(bookService, times(1)).updateBook(bookId, bookRequestDTO);
    }

    @Test
    void testUpdateBook_whenBookNotFound_shouldReturnApiException() throws Exception {
        String bookId = "1";
        String errorMessage = "Book by that ID not found";

        // thenThrow() is used specifically when an error is supposed to be thrown
        when(bookService.updateBook(bookId, bookRequestDTO)).thenThrow(new ApiException(errorMessage, HttpStatus.NOT_FOUND));

        // In the case of an error we only expect to return an error message so that is what we will check
        mockMvc.perform(put("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(errorMessage));
        // still only need to call the bookService updateBook once
        verify(bookService).updateBook(bookId, bookRequestDTO);
    }
}
