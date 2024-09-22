package org.threefour.ddip.product.elasticsearch.domain;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.product.domain.PageInformation;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchProductsResponse {
    private List<SearchProductResponse> searchProductResponses;
    private PageInformation pageInformation;

    private SearchProductsResponse(List<SearchProductResponse> searchProductResponses, PageInformation pageInformation) {
        this.searchProductResponses = searchProductResponses;
        this.pageInformation = pageInformation;
    }

    public static SearchProductsResponse from(Page<ProductDocument> pagedProductDocuments, List<Image> images) {
        List<ProductDocument> productDocuments = pagedProductDocuments.getContent();
        List<SearchProductResponse> searchProductResponses = new ArrayList<>();
        for (int i = 0; i < productDocuments.size(); i++) {
            searchProductResponses.add(SearchProductResponse.from(productDocuments.get(i), images.get(i)));
        }

        return new SearchProductsResponse
                (searchProductResponses, PageInformation.fromProductDocument(pagedProductDocuments)
                );
    }

    public SearchProductResponse get(int index) {
        return searchProductResponses.get(index);
    }
}
