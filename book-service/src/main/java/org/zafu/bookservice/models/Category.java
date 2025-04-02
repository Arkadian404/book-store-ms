package org.zafu.bookservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
@Builder
public class Category extends EntityBase<Integer> {
    @Column(nullable = false)
    private String name;
    private String description;

    @ManyToMany(mappedBy = "categories")
    private List<Book> books = new ArrayList<>();
}
