package com.brayden.firstrestapibooks.service.impl;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.exception.ApiException;
import com.brayden.firstrestapibooks.model.Book;
import com.brayden.firstrestapibooks.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceImplTest {

    // Creates a mocked repository dependency for unit testing.
    @Mock
    private BookRepository bookRepository;

    // Creates the class under test and injects mocked dependencies into it.
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeEach
    void setup() {
        // Through this call, Mockito initializes @Mock and @InjectMocks fields.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateBook_whenBookExists_shouldUpdateAndReturnBook() {
        // This will be used as the request to change the book
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setName("Updated Name");
        bookRequestDTO.setAuthor("Updated Author");
        bookRequestDTO.setPrice("150");

        // Represents the existing book currently stored in the repository.
        Book bookInRepository = new Book();
        bookInRepository.setId("1");
        bookInRepository.setName("Original Name");
        bookInRepository.setAuthor("Original Author");
        bookInRepository.setPrice("100");

        // Represents the updated book returned after save.
        Book updatedBook = new Book();
        updatedBook.setId("1");
        updatedBook.setName("Updated Name");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setPrice("150");

        // Returns an Optional containing the existing book with ID 1.
        when(bookRepository.findById("1")).thenReturn(Optional.of(bookInRepository));
        // this is what will return if a save works the way it should
        when(bookRepository.save(bookInRepository)).thenReturn(updatedBook);

        // Run the service method under test.
        BookResponseDTO result = bookServiceImpl.updateBook("1", bookRequestDTO);
        // Makes sure result is not null.
        assertNotNull(result);
        // Verify result fields match the expected updated values.
        assertEquals("1", result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Author", result.getAuthor());
        assertEquals("150", result.getPrice());
        // Verify repository methods were called as expected.
        verify(bookRepository).findById("1");
        verify(bookRepository).save(bookInRepository);
    }

    @Test
    void testUpdateBook_whenBookNotFound_shouldThrowApiException() {
        // ues the same data as what was used for other test to continue continuity
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setName("Updated Name");
        bookRequestDTO.setAuthor("Updated Author");
        bookRequestDTO.setPrice("150");

        String idTest = "9999";

        when(bookRepository.findById(idTest)).thenReturn(Optional.empty());

//        proves that an exception was thrown
        ApiException apiException = assertThrows(ApiException.class, () -> {
            bookServiceImpl.updateBook(idTest, bookRequestDTO);
        });
        assertEquals("No Book found by id: " + idTest, apiException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, apiException.getHttpStatus());

        verify(bookRepository, never()).save(any(Book.class));
    }
}
