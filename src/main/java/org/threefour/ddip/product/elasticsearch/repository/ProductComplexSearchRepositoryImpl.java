package org.threefour.ddip.product.elasticsearch.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class ProductComplexSearchRepositoryImpl implements ProductComplexSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<ProductDocument> findByCategoryKeyword(String keyword, Pageable pageable) {
        Criteria criteria = Criteria.where("firstCategoryName").startsWith(keyword)
                .or(Criteria.where("secondCategoryName").startsWith(keyword)
                        .or(Criteria.where("thirdCategoryName").startsWith(keyword)));
        Query query = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        List<ProductDocument> productDocuments = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(productDocuments, pageable, searchHits.getTotalHits());
    }

    @Override
    public Page<ProductDocument> findByRegionKeyword(String keyword, Pageable pageable) {
        Criteria criteria = Criteria.where("city").startsWith(keyword)
                .or(Criteria.where("district").startsWith(keyword)
                        .or(Criteria.where("roadAddress").startsWith(keyword)));
        Query query = new CriteriaQuery(criteria).setPageable(pageable);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        List<ProductDocument> productDocuments = searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        return new PageImpl<>(productDocuments, pageable, searchHits.getTotalHits());
    }

    @Override
    public List<ProductDocument> findByStartWithNameKeyword(String keyword) {
        Criteria criteria = Criteria.where("name").startsWith(keyword);
        Query query = new CriteriaQuery(criteria);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        return new ArrayList<>(searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toMap(
                        ProductDocument::getName,
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    @Override
    public List<ProductDocument> findByStartWithTitleKeyword(String keyword) {
        Criteria criteria = Criteria.where("title").startsWith(keyword);
        Query query = new CriteriaQuery(criteria);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        return new ArrayList<>(searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toMap(
                        ProductDocument::getName,
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    @Override
    public List<ProductDocument> findByStartWithCategoryKeyword(String keyword) {
        Criteria criteria = Criteria.where("firstCategoryName").startsWith(keyword)
                .or(Criteria.where("secondCategoryName").startsWith(keyword)
                        .or(Criteria.where("thirdCategoryName").startsWith(keyword)));
        Query query = new CriteriaQuery(criteria);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        return new ArrayList<>(searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toMap(
                        ProductDocument::getThirdCategoryName,
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    @Override
    public List<ProductDocument> findByStartWithRegionKeyword(String keyword) {
        Criteria criteria = Criteria.where("city").startsWith(keyword)
                .or(Criteria.where("district").startsWith(keyword)
                        .or(Criteria.where("roadAddress").startsWith(keyword)));
        Query query = new CriteriaQuery(criteria);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        return new ArrayList<>(searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toMap(
                        ProductDocument::getRoadAddress,
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    @Override
    public List<ProductDocument> findByStartWithSchoolKeyword(String keyword) {
        Criteria criteria = Criteria.where("schoolName").startsWith(keyword);
        Query query = new CriteriaQuery(criteria);
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(query, ProductDocument.class);

        return new ArrayList<>(searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toMap(
                        ProductDocument::getSchoolName,
                        product -> product,
                        (existing, replacement) -> existing
                ))
                .values());
    }
}