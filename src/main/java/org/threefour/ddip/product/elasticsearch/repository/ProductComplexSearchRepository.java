package org.threefour.ddip.product.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;

import java.util.List;

public interface ProductComplexSearchRepository {
    Page<ProductDocument> findByKeywordSortedByRelevance(String keyword, Pageable pageable);

    Page<ProductDocument> findByCategoryKeyword(String categoryKeyword, Pageable pageable);

    Page<ProductDocument> findByRegionKeyword(String keyword, Pageable pageable);

    List<ProductDocument> findByStartWithNameKeyword(String keyword);

    List<ProductDocument> findByStartWithTitleKeyword(String keyword);

    List<ProductDocument> findByStartWithCategoryKeyword(String keyword);

    List<ProductDocument> findByStartWithRegionKeyword(String keyword);

    List<ProductDocument> findByStartWithSchoolKeyword(String keyword);
}
