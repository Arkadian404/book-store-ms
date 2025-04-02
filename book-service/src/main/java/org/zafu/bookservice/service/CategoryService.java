package org.zafu.bookservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zafu.bookservice.dto.request.CategoryRequest;
import org.zafu.bookservice.dto.response.CategoryResponse;
import org.zafu.bookservice.dto.response.CategoryResponse;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.mapper.CategoryMapper;
import org.zafu.bookservice.models.Category;
import org.zafu.bookservice.models.Category;
import org.zafu.bookservice.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CATEGORY-SERVICE")
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryResponse createCategory(CategoryRequest request){
        Category category = mapper.toCategory(request);
        return mapper.toCategoryResponse(repository.save(category));
    }

    public CategoryResponse updateCategory(int id, CategoryRequest request){
        Category category = repository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        mapper.updateCategory(category, request);
        return mapper.toCategoryResponse(repository.save(category));
    }

    public void deleteCategory(int id){
        repository.deleteById(id);
    }

    public List<CategoryResponse> getAll(){
        List<Category> categories = repository.findAll();
        return categories
                .stream()
                .map(mapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getById(int id){
        Category category = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        return mapper.toCategoryResponse(repository.save(category));
    }
}
