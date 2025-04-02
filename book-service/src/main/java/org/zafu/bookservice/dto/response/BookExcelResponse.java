package org.zafu.bookservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookExcelResponse {
    private int totalRows;
    private int successfulRows;
    private int failedRows;
    private List<String> errors;
}
