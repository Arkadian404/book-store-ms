package org.zafu.bookservice.dto.google_book;

import lombok.Data;

@Data
public class SaleInfo {
    private String country;
    private String saleability;
    private ListPrice listPrice;
}
