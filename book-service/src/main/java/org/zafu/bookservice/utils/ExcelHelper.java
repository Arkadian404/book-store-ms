package org.zafu.bookservice.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.zafu.bookservice.dto.request.BookExcelRequest;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.models.Author;
import org.zafu.bookservice.models.Book;
import org.zafu.bookservice.models.Category;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j(topic = "EXCEL-HELPER")
public class ExcelHelper {
    @Value("${cloudinary.default-image}")
    private String defaultImage;
    public final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String[] HEADERS = {
            "Title", "Description", "ISBN", "Page Count", "Image URL", "Language",
            "Price", "Stock Quantity", "Authors", "Categories", "Publisher", "Published Date"
    };


    public boolean isValid(MultipartFile file){
        return Objects.equals(file.getContentType(), TYPE);
    }

    public List<BookExcelRequest> parseExcelBooksData(InputStream stream) {
        List<BookExcelRequest> list = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(stream)) {
            XSSFSheet sheet = workbook.getSheet("book");
            log.info(sheet.getPhysicalNumberOfRows() + " rows");
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                BookExcelRequest request = BookExcelRequest.builder().build();
                int cellIndex = 0;
                for (Cell cell : row) {
                    processCell(cell, request, cellIndex);
                    cellIndex++;
                }
                log.info("Row {}: {}", rowIndex, request);
                log.info(request.toString());
                list.add(request);
                rowIndex++;
            }
        } catch (IOException e) {
            log.error("Error when reading Excel: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_READ_ERROR);
        }
        return list;
    }

    public ByteArrayInputStream exportBooksToExcel(List<Book> books) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Book");
            createHeaderRow(workbook, sheet);
            fillDataRows(sheet, books);
            return new ByteArrayInputStream(writeWB(workbook).toByteArray());
        } catch (IOException e) {
            log.error("Error when exporting books to Excel: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_READ_ERROR);
        }
    }

    private void processCell(Cell cell, BookExcelRequest request, int cellIndex) {
        try {
            switch (cellIndex) {
                case 0 -> request.setTitle(cell.getStringCellValue().trim());
                case 1 -> request.setDescription(cell.getStringCellValue().trim());
                case 2 -> request.setIsbn(cell.getStringCellValue().trim());
                case 3 -> request.setPageCount((int) cell.getNumericCellValue());
                case 4 -> request.setImageUrl(cell.getStringCellValue() != null ? cell.getStringCellValue().trim() : defaultImage);
                case 5 -> request.setLanguage(cell.getStringCellValue().trim());
                case 6 -> request.setPrice(BigDecimal.valueOf(cell.getNumericCellValue()));
                case 7 -> request.setStockQuantity((int) cell.getNumericCellValue());
                case 8 -> request.setAuthors(Arrays.asList(cell.getStringCellValue().split(",\\s*")));
                case 9 -> request.setCategories(Arrays.asList(cell.getStringCellValue().split(",\\s*")));
                case 10 -> request.setPublisher(cell.getStringCellValue().trim());
                case 11 -> request.setPublishedDate(parseDateCell(cell));
                default -> {}
            }
        } catch (Exception e) {
            log.error("Error at cell {}: {}", cellIndex, e.getMessage());
        }
    }

    private String parseDateCell(Cell cell) {
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().toString();
            } else if (cell.getCellType() == CellType.STRING) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                ZonedDateTime zdt = ZonedDateTime.parse(cell.getStringCellValue(), dtf);
                return zdt.toLocalDate().toString();
            }
        } catch (Exception e) {
            log.error("Error when parse date: {}", e.getMessage());
        }
        return null;
    }

    private void createHeaderRow(XSSFWorkbook workbook, XSSFSheet sheet){
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < HEADERS.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(HEADERS[col]);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillDataRows(XSSFSheet sheet, List<Book> books){
        int rowIndex = 1;
        for (Book book : books) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(book.getTitle());
            row.createCell(1).setCellValue(book.getDescription() != null ? book.getDescription() : "");
            row.createCell(2).setCellValue(book.getIsbn());
            row.createCell(3).setCellValue(Math.max(book.getPageCount(), 0));
            row.createCell(4).setCellValue(book.getImageUrl() != null ? book.getImageUrl() : "");
            row.createCell(5).setCellValue(book.getLanguage() != null ? book.getLanguage() : "");
            row.createCell(6).setCellValue(book.getPrice() != null ? book.getPrice().doubleValue() : 0.0);
            row.createCell(7).setCellValue(Math.max(book.getStockQuantity(), 0));
            row.createCell(8).setCellValue(book.getAuthors().stream()
                    .map(Author::getName)
                    .collect(Collectors.joining(", ")));
            row.createCell(9).setCellValue(book.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.joining(", ")));
            row.createCell(10).setCellValue(book.getPublisher() != null ? book.getPublisher().getName() : "");
            row.createCell(11).setCellValue(book.getPublishedDate() != null ? book.getPublishedDate().toString() : "");
        }

        for (int col = 0; col < HEADERS.length; col++) {
            sheet.autoSizeColumn(col);
        }
    }

    private ByteArrayOutputStream writeWB(XSSFWorkbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }


}
