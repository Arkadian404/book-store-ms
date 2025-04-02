package org.zafu.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zafu.bookservice.models.Publisher;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByName(String name);
}
