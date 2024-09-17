package org.threefour.ddip.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.member.domain.Member;
import org.threefour.ddip.member.repository.MemberRepository;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.product.domain.AutoDiscountRequest;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.priceinformation.service.PriceInformationService;
import org.threefour.ddip.product.repository.ProductRepository;
import org.threefour.ddip.util.FormatValidator;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Propagation.NESTED;
import static org.threefour.ddip.image.domain.TargetType.PRODUCT;

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
        Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException(String.format("해당 회원이 존재하지 않습니다. ID: %d", 1)));
        Product product = productRepository.save(Product.from(registerProductRequest, member));

        categoryService.createProductCategories(registerProductRequest.getConnectCategoryRequest(), product);

        if (!FormatValidator.isNoValue(images)) {
            imageService.createImages(PRODUCT, product.getId(), images);
        }

        AutoDiscountRequest autoDiscountRequest = registerProductRequest.getAutoDiscountRequest();
        if (autoDiscountRequest != null) {
            priceInformationService.createPriceInformation(product, autoDiscountRequest);
        }

        return product.getId();
    }
}
