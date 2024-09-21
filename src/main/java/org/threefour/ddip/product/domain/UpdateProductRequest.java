package org.threefour.ddip.product.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.threefour.ddip.product.category.domain.ConnectCategoryRequest;

@NoArgsConstructor
@Getter
@Setter
public class UpdateProductRequest {
    private String id;
    private ConnectCategoryRequest connectCategoryRequest;
    private AutoDiscountRequest autoDiscountRequest;
    private String name;
    private String price;
    private String title;
    private String content;
}
