package org.zafu.bookservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.zafu.bookservice.dto.request.BookRequest;
import org.zafu.bookservice.dto.request.GoogleBookRequest;
import org.zafu.bookservice.dto.response.BookResponse;
import org.zafu.bookservice.models.Book;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toBook(BookRequest request);
    void updateBook(@MappingTarget Book book, BookRequest request);
    BookResponse toBookResponse(Book book);
    List<BookResponse> toBookResponseList(List<Book> books);
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toBook(GoogleBookRequest request);
}
