package org.zafu.bookservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.zafu.bookservice.dto.request.CategoryRequest;

import org.zafu.bookservice.dto.response.CategoryResponse;
import org.zafu.bookservice.models.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);
    void updateCategory(@MappingTarget Category author, CategoryRequest request);
    CategoryResponse toCategoryResponse(Category author);
}
