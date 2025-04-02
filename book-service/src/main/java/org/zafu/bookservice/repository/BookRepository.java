package org.zafu.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.zafu.bookservice.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsBookByIsbn(String isbn);
    List<Book> findByIsbnIn(List<String> list);
}
