package org.zafu.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.zafu.bookservice.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByNameIn(List<String> list);
}
