package org.threefour.ddip.product.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.threefour.ddip.product.category.domain.ConnectCategoryRequest;

@NoArgsConstructor
@Getter
@Setter
public class RegisterProductRequest {
    private String memberId;
    private ConnectCategoryRequest connectCategoryRequest;
    private String name;
    private String price;
    private String title;
    private String content;
    private AutoDiscountRequest autoDiscountRequest;
}
