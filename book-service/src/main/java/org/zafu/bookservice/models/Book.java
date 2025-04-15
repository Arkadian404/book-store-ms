package org.zafu.bookservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(
        name = "books",
        uniqueConstraints = {
                @UniqueConstraint(name = "isbn_uq", columnNames = "isbn")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends EntityBase<Integer> {
    @Column(nullable = false, length = 1250)
    private String title;
    @Column(nullable = false, length = 2500)
    private String slug;
    @Column(length = 10000)
    private String description;
    @Column(nullable = false)
    private String isbn;
    private int pageCount;
    private String imageUrl;
    private String language;
    private BigDecimal price;
    private int stockQuantity;
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;
    private LocalDate publishedDate;



}
