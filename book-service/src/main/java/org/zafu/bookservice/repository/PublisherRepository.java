package org.zafu.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zafu.bookservice.models.Publisher;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByName(String name);

    @Query(
            value = """
                    select distinct p.id, p.name, p.description, p.created_by, p.created_date, p.last_modified_date, p.updated_by
                    from publishers as p
                    join books as b on p.id = b.publisher_id
                    join book_categories as bc on b.id = bc.book_id
                    join categories c on bc.category_id = c.id
                    where c.name = :categoryName
""",
            nativeQuery = true
    )
    List<Publisher> findAllByCategoryName(String categoryName);
}
