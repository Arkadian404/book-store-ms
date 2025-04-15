package org.zafu.bookservice.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.zafu.bookservice.models.Book;
import org.zafu.bookservice.models.Category;


import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsBookByIsbn(String isbn);
    List<Book> findByIsbnIn(List<String> list);
    @Query(value = "select * from books as b order by b.created_date desc limit 4", nativeQuery = true)
    List<Book> findFeaturedBooks();
    @Query(value = "select * from books as b order by stock_quantity asc limit 1", nativeQuery = true)
    Optional<Book> findBestSellingBook();
    @Query(value = "select * from books as b order by published_date desc limit 8", nativeQuery = true)
    List<Book> findAllGenres();
    List<Book> findTop4ByCategoriesIn(List<Category> categories);
    Page<Book> findAllByCategoriesIn(List<Category> categories, Pageable pageable);
    Optional<Book> findBySlug(String slug);
    @Query(value ="""
            select distinct b.id, b.title, b.description, b.page_count, b.image_url, b.isbn, b.published_date, b.language, b.slug, b.stock_quantity, b.price, b.publisher_id, b.created_by, b.created_date, b.last_modified_date, b.updated_by
            from books as b
            join book_categories as bc on b.id = bc.book_id
            join categories c on bc.category_id = c.id
            where c.name = :category
            and b.id != :id
            limit 4
""", nativeQuery = true)
    List<Book> findRelatedBooksByCategoriesIn(String category, Integer id);
}
