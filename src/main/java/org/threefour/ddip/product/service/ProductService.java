package org.threefour.ddip.product.service;

import org.threefour.ddip.product.domain.RegisterProductRequest;

public interface ProductService {
    Long createProduct(RegisterProductRequest registerProductRequest);
}
