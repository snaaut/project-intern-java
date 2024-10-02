package com.project_intern.haibazo_shop.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetail {
    Long id;
    String name;
    String description;
    String style;
    List<String> images;
    double originalPrice;
    double discountedPrice;
    int reviewCount;
    long discountTime;
    List<String> sizes;
    List<String> colors;
}
