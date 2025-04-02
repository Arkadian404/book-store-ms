package org.zafu.bookservice.models;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Document(indexName = "books")
@Data
@Builder
public class BookDocument {
    @Id
    private Integer id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Keyword)
    private String isbn;
    @Field(type = FieldType.Text)
    private String publisher;
    @Field(type = FieldType.Text)
    private BigDecimal price;
    @Field(type = FieldType.Text)
    private String language;
    @Field(type = FieldType.Text)
    private List<String> authors;
    @Field(type = FieldType.Text)
    private List<String> categories;
}
