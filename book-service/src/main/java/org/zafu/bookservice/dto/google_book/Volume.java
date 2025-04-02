package org.zafu.bookservice.dto.google_book;

import lombok.Data;

@Data
public class Volume {
    private String id;
    private VolumeInfo volumeInfo;
    private SaleInfo saleInfo;
}
