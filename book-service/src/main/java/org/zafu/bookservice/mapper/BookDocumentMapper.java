package org.zafu.bookservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.zafu.bookservice.dto.request.BookExcelRequest;
import org.zafu.bookservice.models.Author;
import org.zafu.bookservice.models.Book;
import org.zafu.bookservice.models.BookDocument;
import org.zafu.bookservice.models.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookDocumentMapper {
    @Mapping(source = "publisher", target = "publisher",ignore = true)
    @Mapping(source = "authors", target = "authors",ignore = true)
    @Mapping(source = "categories", target = "categories",ignore = true)
    BookDocument toBookDocument(Book book);
    @Mapping(source = "authors", target = "authors",ignore = true)
    @Mapping(source = "categories", target = "categories",ignore = true)
    BookDocument toBookDocument(BookExcelRequest request);

    @AfterMapping
    default void mapReferences(@MappingTarget BookDocument document, Book book){
        String publisher = book.getPublisher().getName();
        List<String> authors = book.getAuthors()
                .stream()
                .map(Author::getName)
                .toList();
        List<String> categories = book.getCategories()
                .stream()
                .map(Category::getName)
                .toList();
        document.setPublisher(publisher);
        document.setAuthors(authors);
        document.setCategories(categories);
    }
}
