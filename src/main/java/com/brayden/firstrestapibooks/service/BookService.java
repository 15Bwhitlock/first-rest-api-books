package com.brayden.firstrestapibooks.service;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.model.Book;
import java.util.List;

public interface BookService {
    List<BookResponseDTO> findAllBooks();
    BookResponseDTO createBook(BookRequestDTO book);
    BookResponseDTO updateBook(String id, BookRequestDTO book);
    void deleteBook(String id);
}
