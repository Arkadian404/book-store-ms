package org.zafu.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.bookservice.models.Author;

import java.util.List;


public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByNameIn(List<String> authors);
}
