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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override // useful because it confirms this matches a method from the interface and helps avoid typo-based method mismatches
    @Transactional(readOnly = true) // readOnly transactions are for read operations and can reduce unnecessary write-related work
    public List<BookResponseDTO> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(MapperBook::modelToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> findByAuthor(String author) {
        return bookRepository.findBookByAuthor(author).stream()
                .map(MapperBook::modelToResponseDto)
                .toList();
    }

    @Override
    @Transactional // write operations should be transactional so changes are rolled back if an error occurs
    public BookResponseDTO createBook(BookRequestDTO book) {
        Book modelBook = MapperBook.dtoRequestToModel(book);
        Book repositoryBook = bookRepository.save(modelBook);

        // this would let the book object with the wrong name value save to the db if not for @Transactional
        if(book.getName().equals("name")) {
            throw new RuntimeException("Error the value of name can not be 'name'");
        }
        // If you comment out @Transactional for testing, and try to save a book named "name"
        // it is still persisted even when an exception is thrown afterward.
        return MapperBook.modelToResponseDto(repositoryBook);
    }

    @Override
    @Transactional // transactional writes will roll back if an error happens
    public BookResponseDTO updateBook(String id, BookRequestDTO book) {
        // the line below gets the info for the book we want to replace from the db using id
        //and throws an error if that book id does not exist
        Book bookInRepository = bookRepository.findById(id).orElseThrow(() ->
                new ApiException("No Book found by id: " + id, HttpStatus.NOT_FOUND));

        // below fills in info as if it is a new book
        //no need to fill in id since that id is connected to the book we want to replace
        bookInRepository.setName(book.getName());
        bookInRepository.setAuthor(book.getAuthor());
        bookInRepository.setPrice(book.getPrice());

        // now we need to save this new book on the id of the old book replacing/updating it
        Book saveBook = bookRepository.save(bookInRepository);
        // use the MapperBook to model the saveBook response into a bookResponseDTO
        return MapperBook.modelToResponseDto(saveBook);
    }

    @Override
    @Transactional
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
