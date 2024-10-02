package com.project_intern.haibazo_shop.dto;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    Long id;
    String name;
    String description;
    String stype;
    List<String> images;
    double originalPrice;
    double discountedPrice;
}
