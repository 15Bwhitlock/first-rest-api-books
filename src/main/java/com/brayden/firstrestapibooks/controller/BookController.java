package com.brayden.firstrestapibooks.controller;

import com.brayden.firstrestapibooks.dto.BookRequestDTO;
import com.brayden.firstrestapibooks.dto.BookResponseDTO;
import com.brayden.firstrestapibooks.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/books")
@Tag(name = "Books", description = "API for managing books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get the list of all books", description = "Returns a list of all the books in the database")
    @ApiResponse(responseCode = "200", description = "Books successfully retrieved")
    public List<BookResponseDTO> findAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/author/{author}")
    @Operation(summary = "Get the list of books by author", description = "Returns a list of all the books in the database that are by this author")
    @ApiResponse(responseCode = "200", description = "Books by this author successfully retrieved")
    public List<BookResponseDTO> findByAuthor(@PathVariable String author) {
        return bookService.findByAuthor(author);
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    @ApiResponse(responseCode = "201", description = "Books successfully created")
    public BookResponseDTO createBook(@RequestBody BookRequestDTO book) {
        return bookService.createBook(book);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates an existing book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books successfully updated"),
            @ApiResponse(responseCode = "404", description = "Book by that ID not found")
    })
    public BookResponseDTO updateBook(
            @Parameter(description = "ID of the book to by updated", required = true) // use @Parameter to describe the variable needed for updateBook to work
            @PathVariable String id,
            @RequestBody BookRequestDTO book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes an existing book by its ID")
    @ApiResponse(responseCode = "200", description = "Book successfully deleted by ID")
    public void deleteBook(
            @Parameter(description = "ID of the book to by deleted", required = true)
            @PathVariable String id) {
        bookService.deleteBook(id);
    }
}
