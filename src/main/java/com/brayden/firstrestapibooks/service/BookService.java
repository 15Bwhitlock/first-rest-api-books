package com.brayden.firstrestapibooks.service;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;

import java.util.List;

public interface BookService {
    List<BookResponseDTO> findAllBooks();

    List<BookResponseDTO> findByAuthor(String author);

    BookResponseDTO createBook(BookRequestDTO book);

    BookResponseDTO updateBook(String id, BookRequestDTO book);

    void deleteBook(String id);
}
