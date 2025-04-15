package org.zafu.bookservice.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "publishers",
        uniqueConstraints = {
                @UniqueConstraint(name = "publisher_name_uq", columnNames = "name")
        }
)
@Builder
public class Publisher extends EntityBase<Integer> {
    @Column(nullable = false)
    private String name;
    private String description;
    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
}
