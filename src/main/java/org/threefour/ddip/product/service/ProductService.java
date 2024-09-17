package org.threefour.ddip.product.service;

import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.domain.RegisterProductRequest;

import java.util.List;

public interface ProductService {
    Long createProduct(RegisterProductRequest registerProductRequest, List<MultipartFile> images);

    Product getProduct(Long productId);
}
