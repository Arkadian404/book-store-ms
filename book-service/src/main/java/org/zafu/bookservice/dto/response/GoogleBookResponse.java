package org.zafu.bookservice.dto.response;

import lombok.Data;
import org.zafu.bookservice.dto.google_book.Volume;

import java.util.List;

@Data
public class GoogleBookResponse {
    private String kind;
    private int totalItems;
    private List<Volume> items;
}
