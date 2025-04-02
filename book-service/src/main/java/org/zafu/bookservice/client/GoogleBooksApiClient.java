package org.zafu.bookservice.client;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.zafu.bookservice.dto.request.GoogleBookRequest;
import org.zafu.bookservice.dto.response.BookResponse;
import org.zafu.bookservice.dto.response.GoogleBookResponse;
import org.zafu.bookservice.mapper.BookDocumentMapper;
import org.zafu.bookservice.mapper.BookMapper;
import org.zafu.bookservice.models.*;
import org.zafu.bookservice.repository.*;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "GOOGLE-BOOKS-API")
public class GoogleBooksApiClient {
    private final RestTemplate restTemplate;
    @Value("${app.google-book.url}")
    private String url;
    @Value("${app.google-book.api-key}")
    private String apiKey;
    @Value("${cloudinary.default-image}")
    private String defaultImage;

    private final BookDocumentRepository bookDocumentRepository;
    private final BookMapper bookMapper;
    private final BookDocumentMapper bookDocumentMapper;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;


    public GoogleBookResponse searchBooks(String query){
        String queryString = url+"/v1/volumes?q="+query+"&key="+apiKey;
        return restTemplate.getForObject(queryString, GoogleBookResponse.class);
    }

    @Transactional
    public BookResponse addGoogleBook(GoogleBookRequest request){
        Book book = bookMapper.toBook(request);
        book.setImageUrl(request.getImageUrl() == null ? defaultImage : request.getImageUrl());
        passReference(book, request);
        bookRepository.save(book);
        BookDocument bookDocument = bookDocumentMapper.toBookDocument(book);
        bookDocumentRepository.save(bookDocument);
        log.info("Save book to elasticsearch with id {}", book.getId());
        return bookMapper.toBookResponse(book);
    }

    private void passReference(Book book, GoogleBookRequest request){
        String publisherReq = request.getPublisher();
        List<String> categoriesReq = request.getCategories();
        List<String> authorsReq = request.getAuthors();
        Slugify slugify = Slugify.builder().transliterator(true).build();
        book.setSlug(slugify.slugify(book.getTitle()));
        Publisher publisher = publisherRepository.findByName(publisherReq)
                .orElse(publisherRepository.save(Publisher.builder().name(publisherReq).build()));
        book.setPublisher(publisher);
        List<Category> categories = getOrCreateCategories(categoriesReq);
        book.setCategories(categories);
        List<Author> authors =  getOrCreateAuthors(authorsReq);
        book.setAuthors(authors);
    }

    private List<Category> getOrCreateCategories(List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByNameIn(categoryNames);
        List<String> existingNames = categories.stream().map(Category::getName).toList();
        List<Category> newCategories = categoryNames.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> Category.builder().name(name).build())
                .toList();

        if (!newCategories.isEmpty()) {
            categoryRepository.saveAll(newCategories);
            categories.addAll(newCategories);
        }

        return categories;
    }

    private List<Author> getOrCreateAuthors(List<String> authorNames) {
        List<Author> authors = authorRepository.findByNameIn(authorNames);
        List<String> existingNames = authors.stream().map(Author::getName).toList();
        List<Author> newAuthors = authorNames.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> Author.builder().name(name).build())
                .toList();

        if (!newAuthors.isEmpty()) {
            authorRepository.saveAll(newAuthors);
            authors.addAll(newAuthors);
        }

        return authors;
    }

}
