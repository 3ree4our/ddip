package org.threefour.ddip.product.elasticsearch.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;

import java.util.List;

public interface ProductSearchService {
    void saveProductDocument(Product product);

    Page<ProductDocument> getByKeywordRelevance(String keyword, Pageable pageable);

    Page<ProductDocument> getByProductKeyword(String keyword, Pageable pageable);

    Page<ProductDocument> getByTitleKeyword(String keyword, Pageable pageable);

    Page<ProductDocument> getByCategoryKeyword(String keyword, Pageable pageable);

    Page<ProductDocument> getByRegionKeyword(String keyword, Pageable pageable);

    Page<ProductDocument> getBySchoolKeyword(String keyword, Pageable pageable);

    List<ProductDocument> getByStartWithNameKeyword(String keyword);

    List<ProductDocument> getByStartWithTitleKeyword(String keyword);

    List<ProductDocument> getByStartWithCategoryKeyword(String keyword);

    List<ProductDocument> getByStartWithRegionKeyword(String keyword);

    List<ProductDocument> getByStartWithSchoolKeyword(String keyword);
}
