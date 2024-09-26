package org.threefour.ddip.product.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    Page<ProductDocument> findByName(String keyword, Pageable pageable);

    Page<ProductDocument> findByTitle(String keyword, Pageable pageable);

    @Query("{\"prefix\": { \"schoolName\": \"?0\" }}")
    Page<ProductDocument> findBySchoolName(String keyword, Pageable pageable);
}