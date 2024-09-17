package org.threefour.ddip.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.chat.domain.dto.ChatResponseDTO;
import org.threefour.ddip.chat.domain.dto.ChatroomResponseDTO;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.domain.RegisterProductRequest;

import java.util.List;

public interface ProductService {
    Long createProduct(RegisterProductRequest registerProductRequest, List<MultipartFile> images);

    Product getProduct(Long productId);

    Page<Product> getProducts(Pageable pageable, Short categoryId);
}
