package org.threefour.ddip.image.domain;

import org.threefour.ddip.product.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

import static org.threefour.ddip.image.domain.TargetType.PRODUCT;

public class RepresentativeImagesRequest {
    private TargetType targetType;
    private List<Long> targetIds;

    private RepresentativeImagesRequest(TargetType targetType, List<Long> targetIds) {
        this.targetType = targetType;
        this.targetIds = targetIds;
    }

    public static RepresentativeImagesRequest from(List<Product> products) {
        List<Long> targetIds = products.stream().map(Product::getId).collect(Collectors.toList());

        return new RepresentativeImagesRequest(PRODUCT, targetIds);
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public Long get(int index) {
        return targetIds.get(index);
    }

    public int size() {
        return targetIds.size();
    }
}
