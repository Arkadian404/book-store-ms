package org.zafu.bookservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zafu.bookservice.client.GoogleBooksApiClient;
import org.zafu.bookservice.dto.ApiResponse;
import org.zafu.bookservice.dto.PageResponse;
import org.zafu.bookservice.dto.request.GoogleBookRequest;
import org.zafu.bookservice.dto.response.GoogleBookResponse;
import org.zafu.bookservice.dto.request.BookRequest;
import org.zafu.bookservice.dto.request.UpdateStockRequest;
import org.zafu.bookservice.dto.response.BookExcelResponse;
import org.zafu.bookservice.dto.response.BookResponse;
import org.zafu.bookservice.models.BookDocument;
import org.zafu.bookservice.service.BookService;
import org.zafu.bookservice.service.ExcelService;
import org.zafu.bookservice.utils.ExcelHelper;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final ExcelService excelService;
    private final ExcelHelper excelHelper;
    private final GoogleBooksApiClient client;


    @GetMapping
    public ApiResponse<List<BookResponse>> getAllBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getAll())
                .build();
    }


    @GetMapping("/slug/{slug}")
    public ApiResponse<BookResponse> getBookBySlug(@PathVariable String slug){
        return ApiResponse.<BookResponse>builder()
                .message("Get book with slug: " + slug)
                .result(bookService.getBySlug(slug))
                .build();
    }

    @GetMapping("/sides/featured")
    public ApiResponse<List<BookResponse>> getFeaturedBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getFeaturedBooks())
                .build();
    }

    @GetMapping("/sides/best-seller")
    public ApiResponse<BookResponse> getBestSellerBooks(){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.getBestSellingBook())
                .build();
    }

    @GetMapping("/sides/all-genres")
    public ApiResponse<List<BookResponse>> getAllGenresBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getAllGenres())
                .build();
    }

    @GetMapping("/sides/fiction")
    public ApiResponse<List<BookResponse>> getFictionBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getBookByCategoryName("Fiction"))
                .build();
    }

    @GetMapping("/sides/drama")
    public ApiResponse<List<BookResponse>> getDramaBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getBookByCategoryName("Drama"))
                .build();
    }

    @GetMapping("/sides/history")
    public ApiResponse<List<BookResponse>> getHistoryBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getBookByCategoryName("History"))
                .build();
    }

    @GetMapping("/sides/poetry")
    public ApiResponse<List<BookResponse>> getPoetryBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getBookByCategoryName("Poetry"))
                .build();
    }

    @GetMapping("/sides/literary-criticism")
    public ApiResponse<List<BookResponse>> getLiteraryCriticismBooks(){
        return ApiResponse.<List<BookResponse>>builder()
                .result(bookService.getBookByCategoryName("Literary Criticism"))
                .build();
    }

    @GetMapping("/sides/related")
    public ApiResponse<List<BookResponse>> getRelatedBooks(
            @RequestParam String category,
            @RequestParam Integer bookId){
        return ApiResponse.<List<BookResponse>>builder()
                .message("Get related books with category: " + category)
                .result(bookService.getRelatedBooksByCategoriesIn(category, bookId))
                .build();
    }

    @GetMapping("/download")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamResource> downloadFile(){
        ByteArrayInputStream in = excelService.downloadExcelBookFile();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, excelHelper.TYPE);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=books_export.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookExcelResponse> uploadExcelFile(
            @RequestPart MultipartFile file
    ){
        return ApiResponse.<BookExcelResponse>builder()
                .message(String.format("Upload file %s successfully", file.getName()))
                .result(excelService.uploadExcelBookFile(file))
                .build();
    }

    @GetMapping("/page/all")
    public ApiResponse<PageResponse<BookResponse>> getAllBooksPaging(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "12", required = false) int size
    ){
        return ApiResponse.<PageResponse<BookResponse>>builder()
                .result(bookService.getAllBooksPaging(page, size))
                .build();
    }

    @GetMapping("/page/category/{categoryName}")
    public ApiResponse<PageResponse<BookResponse>> getAllBooksPagingCategoryName(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "12", required = false) int size,
            @PathVariable String categoryName
    ){
        return ApiResponse.<PageResponse<BookResponse>>builder()
                .result(bookService.getAllBooksPagingByCategory(page, size, categoryName))
                .build();
    }

    @GetMapping("/page/filter")
    public ApiResponse<PageResponse<BookResponse>> getFilteredBooks(
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "12", required = false) int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String... filters
    ){
        return ApiResponse.<PageResponse<BookResponse>>builder()
                .result(bookService.filterBooksPaging(page, size, sortBy, filters))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<BookDocument>> searchBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "12", required = false) int size
    ){
        return ApiResponse.<PageResponse<BookDocument>>builder()
                .result(bookService.searchBooks(page, size, keyword))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBookById(@PathVariable int id){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.getById(id))
                .build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> createBook(
            @RequestPart @Valid BookRequest request,
            @RequestPart(required = false) MultipartFile image
            ){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.createBook(request, image))
                .build();
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable int id,
            @RequestPart @Valid BookRequest request,
            @RequestPart(required = false) MultipartFile image
    ){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.updateBook(id, request, image))
                .build();
    }

    @PutMapping("/{id}/stock")
    public ApiResponse<BookResponse> updateStock(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateStockRequest request
            ){
        return ApiResponse.<BookResponse>builder()
                .result(bookService.updateStock(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteBook(@PathVariable int id){
        bookService.deleteBook(id);
        return ApiResponse.<Void>builder()
                .message("Delete successfully!")
                .build();
    }

    @GetMapping("/google-books/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<GoogleBookResponse> searchGoogleBooks(@RequestParam String query){
        return ApiResponse.<GoogleBookResponse>builder()
                .message("Searching with google books api")
                .result(client.searchBooks(query))
                .build();
    }


    @PostMapping("/google-books/parse")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BookResponse> parseGoogleBookData(@RequestBody @Valid GoogleBookRequest request){
        return ApiResponse.<BookResponse>builder()
                .message("Parsing data from google books api")
                .result(client.addGoogleBook(request))
                .build();
    }


}
