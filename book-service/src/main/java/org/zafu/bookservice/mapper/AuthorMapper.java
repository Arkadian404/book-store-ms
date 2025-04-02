package org.zafu.bookservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.zafu.bookservice.dto.request.AuthorRequest;
import org.zafu.bookservice.dto.response.AuthorResponse;
import org.zafu.bookservice.models.Author;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toAuthor(AuthorRequest request);
    void updateAuthor(@MappingTarget Author author, AuthorRequest request);
    AuthorResponse toAuthorResponse(Author author);
}
