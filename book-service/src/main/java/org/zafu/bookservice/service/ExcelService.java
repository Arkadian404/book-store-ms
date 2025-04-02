package org.zafu.bookservice.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zafu.bookservice.dto.request.BookExcelRequest;
import org.zafu.bookservice.dto.response.BookExcelResponse;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.models.Book;
import org.zafu.bookservice.utils.ExcelHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "EXCEL-SERVICE")
public class ExcelService {
    private final ExcelHelper excelHelper;
    private final BookService bookService;

    public BookExcelResponse uploadExcelBookFile(MultipartFile file){
        if(!excelHelper.isValid(file)) throw new AppException(ErrorCode.FILE_FORMAT_INCORRECT);
        BookExcelResponse response = new BookExcelResponse();
        List<String> errors = new ArrayList<>();
        try {
            List<BookExcelRequest> bookExcelRequests = excelHelper.parseExcelBooksData(file.getInputStream());
            response.setTotalRows(bookExcelRequests.size());
            List<BookExcelRequest> validRequests = new ArrayList<>();
            for (BookExcelRequest request : bookExcelRequests) {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();
                Set<ConstraintViolation<BookExcelRequest>> violations = validator.validate(request);
                if (!violations.isEmpty()) {
                    errors.add("Row with ISBN " + request.getIsbn() + " failed: " + violations);
                    continue;
                }
                validRequests.add(request);
            }
            bookService.saveExcelBooks(bookExcelRequests);
            response.setSuccessfulRows(validRequests.size());
            response.setFailedRows(bookExcelRequests.size() - validRequests.size());
            response.setErrors(errors);
        } catch (IOException e) {
            log.info("Error when process excel file {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_READ_ERROR);
        }
        return response;
    }

    public ByteArrayInputStream downloadExcelBookFile(){
        List<Book> books = bookService.getBooks();
        return excelHelper.exportBooksToExcel(books);
    }
}
