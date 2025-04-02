package org.zafu.bookservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zafu.bookservice.dto.PageResponse;
import org.zafu.bookservice.dto.request.BookExcelRequest;
import org.zafu.bookservice.dto.request.BookRequest;
import org.zafu.bookservice.dto.request.UpdateStockRequest;
import org.zafu.bookservice.dto.response.BookResponse;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.mapper.BookDocumentMapper;
import org.zafu.bookservice.mapper.BookMapper;
import org.zafu.bookservice.models.*;
import org.zafu.bookservice.repository.*;
import org.zafu.bookservice.repository.criteria.FilterCriteria;
import org.zafu.bookservice.repository.criteria.FilterCriteriaConsumer;
import org.zafu.bookservice.utils.CloudinaryService;
import org.zafu.bookservice.utils.ExcelHelper;


import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "BOOK-SERVICE")
public class BookService {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BookRepository repository;
    private final BookDocumentRepository bookDocumentRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper mapper;
    private final BookDocumentMapper bookDocumentMapper;

    @PersistenceContext
    private final EntityManager entityManager;
    @Value("${cloudinary.default-image}")
    private String defaultImage;
    private final CloudinaryService cloudinaryService;

    public List<Book> getBooks(){
        return repository.findAll();
    }

    private void passBookRefs(Book book, BookRequest request){
        int publisherId = request.getPublisherId();
        List<Integer> authorIds = request.getAuthorIds();
        List<Integer> categoryIds = request.getCategoryIds();
        Slugify slugify = Slugify.builder().transliterator(true).build();
        book.setSlug(slugify.slugify(book.getTitle()));
        book.setPublisher(
                publisherRepository.findById(publisherId)
                        .orElseThrow(() -> new AppException(ErrorCode.PUBLISHER_NOT_EXISTED))
        );
        book.setAuthors(authorRepository.findAllById(authorIds));
        book.setCategories(categoryRepository.findAllById(categoryIds));
    }

    @Transactional
    public BookResponse createBook(BookRequest request, MultipartFile image){
            Book book = mapper.toBook(request);
            if(image != null && !image.isEmpty()){
                String imageUrl = cloudinaryService.uploadImage(image);
                book.setImageUrl(imageUrl);
            }else {
                book.setImageUrl(defaultImage);
            }
            passBookRefs(book, request);
            repository.save(book);
            BookDocument bookDocument = bookDocumentMapper.toBookDocument(book);
            bookDocumentRepository.save(bookDocument);
            return mapper.toBookResponse(book);
    }

    @Transactional
    public BookResponse updateBook(int id, BookRequest request, MultipartFile image){
        Book book = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        mapper.updateBook(book, request);
        if(image != null && !image.isEmpty()){
            String imageUrl = cloudinaryService.uploadImage(image);
            book.setImageUrl(imageUrl);
        }
        passBookRefs(book, request);
        repository.save(book);
        BookDocument bookDocument = bookDocumentMapper.toBookDocument(book);
        bookDocumentRepository.save(bookDocument);
        return mapper.toBookResponse(book);
    }

    public void deleteBook(int id){
        Book book = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        book.getAuthors().clear();
        book.getCategories().clear();
        repository.save(book);
        repository.delete(book);
        bookDocumentRepository.deleteById(id);
    }

    public List<BookResponse> getAll(){
        List<Book> books = repository.findAll();
        return books
                .stream()
                .map(mapper::toBookResponse)
                .toList();
    }

    public BookResponse getById(int id){
        Book book = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        return mapper.toBookResponse(repository.save(book));
    }

    public PageResponse<BookResponse> getAllBooksPaging(int page, int size){
        if(page<1 || size <1) throw new AppException(ErrorCode.PAGE_OR_SIZE_MUST_BE_VALID);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Book> bookPage = repository.findAll(pageable);
        List<BookResponse> data = bookPage.getContent().stream()
                .map(mapper::toBookResponse)
                .toList();
        return PageResponse.<BookResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(bookPage.getTotalPages())
                .totalElements(bookPage.getTotalElements())
                .data(data)
                .build();
    }

    public PageResponse<BookResponse> filterBooksPaging(int page, int size, String sortBy, String... filters){
        if(page<1 || size <1) throw new AppException(ErrorCode.PAGE_OR_SIZE_MUST_BE_VALID);
        List<FilterCriteria> criteriaList = new ArrayList<>();
        if(filters!=null){
            for(String s : filters){
                Pattern pattern = Pattern.compile("([\\w.]+)([:><])(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    criteriaList.add(new FilterCriteria(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3)
                    ));
                }
            }
        }
        Page<Book> books = getFilteredBooks(page, size, sortBy, criteriaList);
        return PageResponse.<BookResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(books.getTotalPages())
                .totalElements(books.getTotalElements())
                .data(books.getContent().stream().map(mapper::toBookResponse).toList())
                .build();
    }

    private Page<Book> getFilteredBooks(int page, int size, String sortBy, List<FilterCriteria> criteriaList) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        Predicate predicate = buildPredicate(builder, root, criteriaList);
        query.where(predicate);
        query.distinct(true);

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(3);
                if (value.equalsIgnoreCase("desc")) {
                    query.orderBy(builder.desc(root.get(key)));
                } else {
                    query.orderBy(builder.asc(root.get(key)));
                }
            }
        }

        long totalElements = getTotalElements(criteriaList, builder, root, query);
        log.info("Total elements {}", totalElements);

        List<Book> books = entityManager.createQuery(query)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

        return new PageImpl<>(books, PageRequest.of(page - 1, size), totalElements);
    }

    private long getTotalElements(List<FilterCriteria> criteriaList, CriteriaBuilder builder, Root<Book> root, CriteriaQuery<Book> query) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Book> countRoot = countQuery.from(Book.class);
        countQuery.select(builder.count(countRoot));
        Predicate countPredicate = buildPredicate(builder, root, criteriaList);
        query.where(countPredicate);

       return entityManager.createQuery(countQuery).getSingleResult();
    }

    private Predicate buildPredicate(CriteriaBuilder builder, Root<Book> root, List<FilterCriteria> criteriaList) {
        FilterCriteriaConsumer consumer = new FilterCriteriaConsumer(builder, root);
        criteriaList.forEach(consumer);
        return consumer.getPredicate();
    }

    public PageResponse<BookDocument> searchBooks(int page, int size, String keyword){
        if(page<1 || size <1) throw new AppException(ErrorCode.PAGE_OR_SIZE_MUST_BE_VALID);
        NativeQuery query = null;
        log.info("Start searching!!!");
        if(keyword!=null && !keyword.isEmpty()){
            query = NativeQuery.builder()
                    .withQuery(q -> q.bool(b -> b
                            .should(s -> s.match(
                                    m-> m.field("title")
                                            .query(keyword)
                                            .fuzziness("AUTO")
                                            .minimumShouldMatch("80%")
                                            .boost(2.0F)))
                            .should(s -> s.match(
                                    m-> m.field("publisher")
                                            .query(keyword)
                                            .fuzziness("AUTO")
                                            .minimumShouldMatch("80%")
                                            .boost(2.0F)))
                            .should(s -> s.match(
                                    m-> m.field("authors")
                                            .query(keyword)
                                            .fuzziness("AUTO")
                                            .minimumShouldMatch("80%")
                                            .boost(2.0F)))
                            .should(s -> s.match(
                                    m-> m.field("categories")
                                            .query(keyword)
                                            .fuzziness("AUTO")
                                            .minimumShouldMatch("80%")
                                            .boost(2.0F)))
                            .should(s -> s.matchPhrase(m-> m.field("isbn").query(keyword)))
                            .should(s-> s.match(m-> m.field("language").query(keyword)))
                    ))
                    .withPageable(PageRequest.of(page-1, size))
                    .build();
        }else{
            query = NativeQuery.builder()
                    .withQuery(q -> q.matchAll(m -> m))
                    .withPageable(PageRequest.of(page - 1, size))
                    .build();
        }
        SearchHits<BookDocument> searchHits = elasticsearchTemplate.search(query, BookDocument.class);
        log.info(searchHits.toString());
        List<BookDocument> data = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
        long totalElements = searchHits.getTotalHits();
        return PageResponse.<BookDocument>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .totalElements(totalElements)
                .data(data)
                .build();
    }

    @Transactional
    public void saveExcelBooks(List<BookExcelRequest> list) {
        Map<String, Author> authorMap = getOrCreateAuthorsBatch(list);
        Map<String, Category> categoryMap = getOrCreateCategoriesBatch(list);

        List<Book> books = convertExcelToBooks(list);
        if (!books.isEmpty()) {
            books.forEach(book -> {
                Slugify slugify = Slugify.builder().transliterator(true).build();
                book.setSlug(slugify.slugify(book.getTitle()));
                List<Author> authors = book.getAuthors().stream()
                        .map(author -> authorMap.get(author.getName()))
                        .filter(Objects::nonNull)
                        .toList();
                book.setAuthors(authors);
                List<Category> categories = book.getCategories().stream()
                        .map(category -> categoryMap.get(category.getName()))
                        .filter(Objects::nonNull)
                        .toList();
                book.setCategories(categories);
            });

            List<Book> savedBooks = repository.saveAll(books);
            List<BookDocument> bookDocuments = savedBooks.stream()
                    .map(bookDocumentMapper::toBookDocument)
                    .toList();
            bookDocumentRepository.saveAll(bookDocuments);

            log.info("Saved {} books to database and Elasticsearch", savedBooks.size());
        }
    }

    private List<Book> convertExcelToBooks(List<BookExcelRequest> requests) {
        List<String> isbns = requests.stream().map(BookExcelRequest::getIsbn).toList();
        List<String> existingIsbns = repository.findByIsbnIn(isbns).stream().map(Book::getIsbn).toList();

        List<Book> books = new ArrayList<>();
        for (BookExcelRequest request : requests) {
            if (existingIsbns.contains(request.getIsbn())) {
                log.info("Skip book {} cause ISBN has been existed", request.getIsbn());
                continue;
            }
            Book book = new Book();
            book.setTitle(request.getTitle());
            book.setDescription(request.getDescription());
            book.setIsbn(request.getIsbn());
            book.setPageCount(request.getPageCount());
            book.setImageUrl(request.getImageUrl());
            book.setLanguage(request.getLanguage());
            book.setPrice(request.getPrice());
            book.setStockQuantity(request.getStockQuantity());
            book.setPublishedDate(LocalDate.parse(request.getPublishedDate()));
            book.setPublisher(getPublisher(request.getPublisher()));
            List<Author> authors = request.getAuthors().stream()
                    .map(name -> Author.builder().name(name).build())
                    .toList();
            book.setAuthors(authors);

            List<Category> categories = request.getCategories().stream()
                    .map(name -> Category.builder().name(name).build())
                    .toList();
            book.setCategories(categories);

            books.add(book);
        }
        return books;
    }

    private Map<String, Author> getOrCreateAuthorsBatch(List<BookExcelRequest> requests) {
        Set<String> allAuthorNames = requests.stream()
                .flatMap(req -> req.getAuthors().stream())
                .collect(Collectors.toSet());

        List<Author> existingAuthors = authorRepository.findByNameIn(new ArrayList<>(allAuthorNames));
        Map<String, Author> authorMap = existingAuthors.stream()
                .collect(Collectors.toMap(Author::getName, author -> author));

        List<Author> newAuthors = allAuthorNames.stream()
                .filter(name -> !authorMap.containsKey(name))
                .map(name -> Author.builder().name(name).build())
                .toList();
        if (!newAuthors.isEmpty()) {
            authorRepository.saveAll(newAuthors);
            newAuthors.forEach(author -> authorMap.put(author.getName(), author));
        }
        return authorMap;
    }

    private Map<String, Category> getOrCreateCategoriesBatch(List<BookExcelRequest> requests) {
        Set<String> allCategoryNames = requests.stream()
                .flatMap(req -> req.getCategories().stream())
                .collect(Collectors.toSet());

        List<Category> existingCategories = categoryRepository.findByNameIn(new ArrayList<>(allCategoryNames));
        Map<String, Category> categoryMap = existingCategories.stream()
                .collect(Collectors.toMap(Category::getName, category -> category));

        List<Category> newCategories = allCategoryNames.stream()
                .filter(name -> !categoryMap.containsKey(name))
                .map(name -> Category.builder().name(name).build())
                .toList();
        if (!newCategories.isEmpty()) {
            categoryRepository.saveAll(newCategories);
            newCategories.forEach(category -> categoryMap.put(category.getName(), category));
        }
        return categoryMap;
    }

    private Publisher getPublisher(String publisherName) {
        return publisherRepository.findByName(publisherName)
                .orElseGet(() -> {
                    Publisher newPublisher = Publisher.builder()
                            .name(publisherName)
                            .build();
                    return publisherRepository.save(newPublisher);
                });
    }

    @Transactional
    public BookResponse updateStock(Integer id, UpdateStockRequest request) {
        Book book = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        if(book.getStockQuantity() < request.getQuantity()){
            throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
        }
        book.setStockQuantity(book.getStockQuantity() - request.getQuantity());
        return mapper.toBookResponse(repository.save(book));
    }
}
