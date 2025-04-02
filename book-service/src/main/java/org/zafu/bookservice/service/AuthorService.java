package org.zafu.bookservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zafu.bookservice.dto.request.AuthorRequest;
import org.zafu.bookservice.dto.response.AuthorResponse;
import org.zafu.bookservice.dto.response.AuthorResponse;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.mapper.AuthorMapper;
import org.zafu.bookservice.models.Author;

import org.zafu.bookservice.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHOR-SERVICE")
public class AuthorService {
    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    public AuthorResponse createAuthor(AuthorRequest request){
        Author author = mapper.toAuthor(request);
        return mapper.toAuthorResponse(repository.save(author));
    }

    public AuthorResponse updateAuthor(int id, AuthorRequest request){
        Author author = repository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.AUTHOR_NOT_EXISTED));
        mapper.updateAuthor(author, request);
        return mapper.toAuthorResponse(repository.save(author));
    }

    public void deleteAuthor(int id){
        repository.deleteById(id);
    }

    public List<AuthorResponse> getAll(){
        List<Author> authors = repository.findAll();
        return authors
                .stream()
                .map(mapper::toAuthorResponse)
                .toList();
    }

    public AuthorResponse getById(int id){
        Author author = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        return mapper.toAuthorResponse(repository.save(author));
    }
}
