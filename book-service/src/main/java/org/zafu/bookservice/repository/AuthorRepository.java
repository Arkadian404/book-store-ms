package org.zafu.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zafu.bookservice.models.Author;

import java.util.List;


public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findByNameIn(List<String> authors);

    @Query(value = "select distinct a.id, a.name, a.description, a.created_by, a.created_date, a.last_modified_date, a.updated_by from authors as a join book_authors as ba on a.id = ba.author_id join book_categories as bc on ba.book_id = bc.book_id join categories c on bc.category_id = c.id where c.name = :category",
            nativeQuery = true)
    List<Author> findAllByCategory(String category);
}
