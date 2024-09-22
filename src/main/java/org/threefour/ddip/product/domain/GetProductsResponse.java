package org.threefour.ddip.product.domain;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetProductsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<GetProductResponse> getProductResponses;
    private PageInformation pageInformation;

    private GetProductsResponse(List<GetProductResponse> getProductResponses, PageInformation pageInformation) {
        this.getProductResponses = getProductResponses;
        this.pageInformation = pageInformation;
    }

    public static GetProductsResponse from(Page<Product> pagedProducts, List<Image> images) {
        List<Product> products = pagedProducts.getContent();
        List<GetProductResponse> getProductResponses = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            getProductResponses.add(GetProductResponse.from(products.get(i), List.of(images.get(i))));
        }

        return new GetProductsResponse(getProductResponses, PageInformation.fromProduct(pagedProducts));
    }

    public static GetProductsResponse fromSearch(Page<ProductDocument> pagedProductDocuments, List<Image> images) {
        List<ProductDocument> productDocuments = pagedProductDocuments.getContent();
        List<GetProductResponse> getProductResponses = new ArrayList<>();
        for (int i = 0; i < productDocuments.size(); i++) {
            getProductResponses.add(GetProductResponse.from(productDocuments.get(i), List.of(images.get(i))));
        }

        return new GetProductsResponse(getProductResponses, PageInformation.fromProductDocument(pagedProductDocuments));
    }

    public GetProductResponse get(int index) {
        return getProductResponses.get(index);
    }
}
