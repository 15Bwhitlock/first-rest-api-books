package com.brayden.firstrestapibooks.service.impl;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.exception.ApiException;
import com.brayden.firstrestapibooks.mapper.MapperBook;
import com.brayden.firstrestapibooks.model.Book;
import com.brayden.firstrestapibooks.repository.BookRepository;
import com.brayden.firstrestapibooks.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public List<BookResponseDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(MapperBook::modelToResponseDto)
                .toList();
    }

    @Override
    public List<BookResponseDTO> findByAuthor(String author) {
        return bookRepository.findBookByAuthor(author).stream()
                .map(MapperBook::modelToResponseDto)
                .toList();
    }

    @Override
    public BookResponseDTO createBook(BookRequestDTO book) {
        Book modelBook = MapperBook.dtoRequestToModel(book);
        Book repositoryBook = bookRepository.save(modelBook);
        return MapperBook.modelToResponseDto(repositoryBook);
    }

    @Override
    public BookResponseDTO updateBook(String id, BookRequestDTO book) {
        //the line bellow both gets the info for the book we want to replace from the db using id
        //and throws an error if that book id does not exist
        Book bookInRepository = bookRepository.findById(id).orElseThrow(() ->
                new ApiException("No Book found by id: " + id, HttpStatus.NOT_FOUND));

        //bellow fills in info as if it is a new book
        //no need to fill in id since that id is connected to the book we want to replace
        bookInRepository.setName(book.getName());
        bookInRepository.setAuthor(book.getAuthor());
        bookInRepository.setPrice(book.getPrice());

        //now we need to save this new book on the id of the old book replaceing/updateing it
        Book saveBook = bookRepository.save(bookInRepository);
        //use the MapperBook to model the saveBook response into a bookResponseDTO
        return MapperBook.modelToResponseDto(saveBook);
    }

    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
