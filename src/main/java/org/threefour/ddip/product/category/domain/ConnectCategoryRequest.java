package org.threefour.ddip.product.category.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ConnectCategoryRequest {
    private String firstCategoryId;
    private String secondCategoryId;
    private String thirdCategoryId;
}
