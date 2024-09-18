package org.threefour.ddip.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.domain.AddImagesRequest;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.category.domain.ConnectCategoryRequest;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.product.domain.AutoDiscountRequest;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.domain.UpdateProductRequest;
import org.threefour.ddip.product.exception.ProductNotFoundException;
import org.threefour.ddip.product.priceinformation.service.PriceInformationService;
import org.threefour.ddip.product.repository.ProductRepository;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.READ_UNCOMMITTED;
import static org.springframework.transaction.annotation.Propagation.NESTED;
import static org.threefour.ddip.image.domain.TargetType.PRODUCT;
import static org.threefour.ddip.product.exception.ExceptionMessage.PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final PriceInformationService priceInformationService;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(isolation = READ_COMMITTED, propagation = NESTED, timeout = 20)
    public Long createProduct(RegisterProductRequest registerProductRequest, List<MultipartFile> images) {
        // TODO: 회원 연결
        Product product = productRepository.save(
                Product.from(registerProductRequest, memberRepository.findById(1L).get())
        );

        categoryService.createProductCategories(registerProductRequest.getConnectCategoryRequest(), product);

        if (images != null && !images.isEmpty()) {
            imageService.createImages(AddImagesRequest.from(images, PRODUCT.name(), product.getId().toString()));
        }

        AutoDiscountRequest autoDiscountRequest = registerProductRequest.getAutoDiscountRequest();
        if (autoDiscountRequest != null) {
            priceInformationService.createPriceInformation(product, autoDiscountRequest);
        }

        return product.getId();
    }

    @Override
    @Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 10)
    public Product getProduct(Long productId) {
        return productRepository.findByIdAndDeleteYnFalse(productId).orElseThrow(
                () -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE, productId))
        );
    }

    @Override
    @Transactional(isolation = READ_UNCOMMITTED, readOnly = true, timeout = 20)
    public Page<Product> getProducts(Pageable pageable, Short categoryId) {
        if (categoryId == 0) {
            return productRepository.findByDeleteYnFalse(pageable);
        }

        return productRepository.findByCategoryIdAndDeleteYnFalse(categoryId, pageable);
    }

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 10)
    public void update(UpdateProductRequest updateProductRequest) {
        Product product = getProduct(FormatConverter.parseToLong(updateProductRequest.getId()));

        ConnectCategoryRequest connectCategoryRequest = updateProductRequest.getConnectCategoryRequest();
        if (FormatValidator.hasValue(connectCategoryRequest)) {
            categoryService.updateProductCategories(connectCategoryRequest, product);
            return;
        }

        product.update(updateProductRequest);
        productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        Product product = getProduct(id);
        product.delete();
        productRepository.save(product);
    }
}
