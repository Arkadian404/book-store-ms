package org.zafu.bookservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
@Builder
public class Author extends EntityBase<Integer> {
    @Column(nullable = false)
    private String name;
    private String description;
    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
}
