package org.zafu.bookservice.dto.google_book;

import lombok.Data;

import java.util.List;

@Data
public class VolumeInfo {
    private String title;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifier> industryIdentifiers;
    private int pageCount;
    private List<String> categories;
    private ImageLinks imageLinks;
    private String language;
}
