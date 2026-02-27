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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    // ---- findAllBooks ----

    @Test
    void testFindAllBooks_whenBooksExist_shouldReturnListOfBookResponseDTO() {
        Book modelBook = new Book();
        modelBook.setId("1");
        modelBook.setName("Sample Book");
        modelBook.setAuthor("Author Name");
        modelBook.setPrice("10.99");

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId("1");
        responseDTO.setName("Sample Book");
        responseDTO.setAuthor("Author Name");
        responseDTO.setPrice("10.99");

        when(bookRepository.findAll()).thenReturn(List.of(modelBook));

        List<BookResponseDTO> expectedResponse = List.of(responseDTO);

        List<BookResponseDTO> result = bookServiceImpl.findAllBooks();

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        assertEquals(expectedResponse.size(), result.size());

        verify(bookRepository).findAll();
    }

    @Test
    void testFindAllBooks_whenNoBooksExist_shouldReturnEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookResponseDTO> result = bookServiceImpl.findAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository).findAll();
    }

    // ---- createBook ----

    @Test
    void testCreateBook_whenValidBook_shouldReturnBookResponseDTO() {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setName("Sample Book");
        bookRequestDTO.setAuthor("Author Name");
        bookRequestDTO.setPrice("10.99");

        Book modelBook = new Book();
        modelBook.setId("1");
        modelBook.setName("Sample Book");
        modelBook.setAuthor("Author Name");
        modelBook.setPrice("10.99");

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId("1");
        responseDTO.setName("Sample Book");
        responseDTO.setAuthor("Author Name");
        responseDTO.setPrice("10.99");

        when(bookRepository.save(any(Book.class))).thenReturn(modelBook);

        BookResponseDTO result = bookServiceImpl.createBook(bookRequestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getName(), result.getName());
        assertEquals(responseDTO.getAuthor(), result.getAuthor());
        assertEquals(responseDTO.getPrice(), result.getPrice());

        verify(bookRepository).save(any(Book.class));
    }

    // ---- updateBook ----

    @Test
    void testUpdateBook_whenBookExists_shouldUpdateAndReturnBook() {
        // This will be used as the request to change the book.
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
        // This is what save() should return when successful.
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
        // Uses the same data as the other test to keep continuity.
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setName("Updated Name");
        bookRequestDTO.setAuthor("Updated Author");
        bookRequestDTO.setPrice("150");

        String idTest = "9999";

        when(bookRepository.findById(idTest)).thenReturn(Optional.empty());

        // Proves that an exception was thrown.
        ApiException apiException = assertThrows(ApiException.class, () -> {
            bookServiceImpl.updateBook(idTest, bookRequestDTO);
        });
        assertEquals("No Book found by id: " + idTest, apiException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, apiException.getHttpStatus());

        verify(bookRepository, never()).save(any(Book.class));
    }

    // ---- findByAuthor ----

    @Test
    void testFindByAuthor_whenBooksExist_shouldReturnListOfBookResponseDTO() {
        Book modelBook = new Book();
        modelBook.setId("1");
        modelBook.setName("Sample Book");
        modelBook.setAuthor("Author Name");
        modelBook.setPrice("10.99");

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId("1");
        responseDTO.setName("Sample Book");
        responseDTO.setAuthor("Author Name");
        responseDTO.setPrice("10.99");

        when(bookRepository.findBookByAuthor("Author Name")).thenReturn(List.of(modelBook));

        List<BookResponseDTO> expectedResponse = List.of(responseDTO);

        List<BookResponseDTO> result = bookServiceImpl.findByAuthor("Author Name");

        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());
        assertEquals(expectedResponse, result);

        verify(bookRepository).findBookByAuthor("Author Name");
    }

    @Test
    void testFindByAuthor_whenNoBooksExist_shouldReturnEmptyList() {
        when(bookRepository.findBookByAuthor("Nonexistent Author")).thenReturn(Collections.emptyList());

        List<BookResponseDTO> result = bookServiceImpl.findByAuthor("Nonexistent Author");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository).findBookByAuthor("Nonexistent Author");
    }

    // ---- deleteBook ----

    //TODO: You need to implement 2 tests for the `deleteBook` method of the service:
    // 1. When the Book entity is successfully deleted.
    // 2. When the entity with the given `ID` is not found, and handle the error.

}
