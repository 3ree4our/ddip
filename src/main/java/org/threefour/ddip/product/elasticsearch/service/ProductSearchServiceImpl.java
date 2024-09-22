package org.threefour.ddip.product.elasticsearch.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;
import org.threefour.ddip.product.elasticsearch.repository.ProductComplexSearchRepository;
import org.threefour.ddip.product.elasticsearch.repository.ProductSearchRepository;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.READ_UNCOMMITTED;

@Service
@AllArgsConstructor
@Transactional(isolation = READ_UNCOMMITTED, readOnly = true, timeout = 30)
public class ProductSearchServiceImpl implements ProductSearchService {
    private final ProductSearchRepository productSearchRepository;
    private final ProductComplexSearchRepository productComplexSearchRepository;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 20)
    public void saveProductDocument(Product product) {
        productSearchRepository.save(ProductDocument.from(product));
    }

    @Override
    public Page<ProductDocument> getByProductKeyword(String keyword, Pageable pageable) {
        return productSearchRepository.findByName(keyword, pageable);
    }

    @Override
    public Page<ProductDocument> getByTitleKeyword(String keyword, Pageable pageable) {
        return productSearchRepository.findByTitle(keyword, pageable);
    }

    @Override
    public Page<ProductDocument> getByCategoryKeyword(String keyword, Pageable pageable) {
        return productComplexSearchRepository.findByCategoryKeyword(keyword, pageable);
    }

    @Override
    public Page<ProductDocument> getByRegionKeyword(String keyword, Pageable pageable) {
        return productComplexSearchRepository.findByRegionKeyword(keyword, pageable);
    }

    @Override
    public Page<ProductDocument> getBySchoolKeyword(String keyword, Pageable pageable) {
        return productSearchRepository.findBySchoolName(keyword, pageable);
    }

    @Override
    public List<ProductDocument> getByStartWithNameKeyword(String keyword) {
        return productComplexSearchRepository.findByStartWithNameKeyword(keyword);
    }

    @Override
    public List<ProductDocument> getByStartWithTitleKeyword(String keyword) {
        return productComplexSearchRepository.findByStartWithTitleKeyword(keyword);
    }

    @Override
    public List<ProductDocument> getByStartWithCategoryKeyword(String keyword) {
        return productComplexSearchRepository.findByStartWithCategoryKeyword(keyword);
    }

    @Override
    public List<ProductDocument> getByStartWithRegionKeyword(String keyword) {
        return productComplexSearchRepository.findByStartWithRegionKeyword(keyword);
    }

    @Override
    public List<ProductDocument> getByStartWithSchoolKeyword(String keyword) {
        return productComplexSearchRepository.findByStartWithSchoolKeyword(keyword);
    }
}
