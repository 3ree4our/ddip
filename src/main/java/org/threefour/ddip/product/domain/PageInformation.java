package org.threefour.ddip.product.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.threefour.ddip.deal.domain.Deal;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;
import org.threefour.ddip.util.FormatConverter;

import java.io.Serializable;

@Getter
public class PageInformation implements Serializable {
    private static final long serialVersionUID = 6L;

    private Long totalElements;
    private int totalPages;
    private int size;
    private int pageNumber;
    private boolean isFirst;
    private boolean isLast;

    @Builder
    private PageInformation(
            Long totalElements, int totalPages, int size, int pageNumber, boolean isFirst, boolean isLast
    ) {
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
        this.pageNumber = pageNumber;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

    public static PageInformation fromProduct(Page<Product> pagedProducts) {
        return PageInformation.builder()
                .totalElements(pagedProducts.getTotalElements())
                .totalPages(pagedProducts.getTotalPages())
                .size(pagedProducts.getSize())
                .pageNumber(pagedProducts.getNumber())
                .isFirst(pagedProducts.isFirst())
                .isLast(pagedProducts.isLast())
                .build();
    }

    public static PageInformation fromProductDocument(Page<ProductDocument> pagedProductDocuments) {
        return PageInformation.builder()
                .totalElements(pagedProductDocuments.getTotalElements())
                .totalPages(pagedProductDocuments.getTotalPages())
                .size(pagedProductDocuments.getSize())
                .pageNumber(pagedProductDocuments.getNumber())
                .isFirst(pagedProductDocuments.isFirst())
                .isLast(pagedProductDocuments.isLast())
                .build();
    }

    public static PageInformation fromDeal(Page<Deal> pagedDeals) {
        return PageInformation.builder()
                .totalElements(pagedDeals.getTotalElements())
                .totalPages(pagedDeals.getTotalPages())
                .size(pagedDeals.getSize())
                .pageNumber(pagedDeals.getNumber())
                .isFirst(pagedDeals.isFirst())
                .isLast(pagedDeals.isLast())
                .build();
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = FormatConverter.parseToInt(pageNumber);
    }
}
