package com.brayden.firstrestapibooks.repository;

import com.brayden.firstrestapibooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    // Spring Data JPA can derive this query from the method name without @Query.
    // But if you need explicit SQL, use:
    // @Query(value = "SELECT * FROM book WHERE author = :author", nativeQuery = true)
    // List<Book> findBookByAuthor(@Param("author") String author);

    // By default, @Query uses JPQL (entity/field names, not table/column names).
    // This project's entity name is "book", so the JPQL query uses "book" as the root.
    // @Query("SELECT b FROM book b WHERE b.author = :author")
    // List<Book> findBookByAuthor(@Param("author") String author);

    // Best to use equivalent derived query form (without @Query):
     List<Book> findBookByAuthor(String author);
}
