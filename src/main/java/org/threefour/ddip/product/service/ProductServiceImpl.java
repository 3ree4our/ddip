package org.threefour.ddip.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.threefour.ddip.chat.domain.dto.ProductResponseDTO;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public Long createProduct(RegisterProductRequest registerProductRequest) {
    return productRepository.save(Product.from(registerProductRequest, new Member())).getId();
  }

  // 추후 삭제
  public ProductResponseDTO getProductByProductId(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow();
    return new ProductResponseDTO(productId, product.getTitle(), product.getSeller().getId());
  }

  // 추후 삭제
  public List<ProductResponseDTO> getAllProductBySellerId(Long sellerId) {
    List<Product> products = productRepository.findBySellerId(sellerId);
    List<ProductResponseDTO> productListResponseDTO = new ArrayList<>();
    for (Product p : products) {
      productListResponseDTO.add(new ProductResponseDTO(p.getId(), p.getTitle(), p.getSeller().getId()));
    }
    return productListResponseDTO;
  }

}
