package org.threefour.ddip.product.elasticsearch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.product.domain.GetProductsResponse;
import org.threefour.ddip.product.elasticsearch.domain.ProductDocument;
import org.threefour.ddip.product.elasticsearch.service.ProductSearchService;
import org.threefour.ddip.product.elasticsearch.util.AddressModifier;
import org.threefour.ddip.util.PageableGenerator;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.threefour.ddip.util.PaginationConstant.*;
import static org.threefour.ddip.util.SessionConstant.GET_PRODUCTS_RESPONSE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductSearchController {
    private final ProductSearchService productSearchService;
    private final ImageService imageService;

    @GetMapping("/name")
    public ResponseEntity<Void> searchByProductNameKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = ZERO) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            HttpSession httpSession
    ) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<ProductDocument> productDocuments = productSearchService.getByProductKeyword(keyword, pageable);
        List<Image> representativeImages = imageService
                .getRepresentativeImages(RepresentativeImagesRequest.fromSearch(productDocuments.getContent()));
        GetProductsResponse getProductsResponse = GetProductsResponse.fromSearch(productDocuments, representativeImages);
        httpSession.setAttribute(GET_PRODUCTS_RESPONSE, getProductsResponse);


        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/title")
    public ResponseEntity<Void> searchByTitleKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = ZERO) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            HttpSession httpSession
    ) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<ProductDocument> productDocuments = productSearchService.getByTitleKeyword(keyword, pageable);
        List<Image> representativeImages = imageService
                .getRepresentativeImages(RepresentativeImagesRequest.fromSearch(productDocuments.getContent()));
        GetProductsResponse getProductsResponse = GetProductsResponse.fromSearch(productDocuments, representativeImages);
        httpSession.setAttribute(GET_PRODUCTS_RESPONSE, getProductsResponse);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/category")
    public ResponseEntity<Void> searchByCategoryNameKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = ZERO) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            HttpSession httpSession
    ) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<ProductDocument> productDocuments = productSearchService.getByCategoryKeyword(keyword, pageable);
        List<Image> representativeImages = imageService
                .getRepresentativeImages(RepresentativeImagesRequest.fromSearch(productDocuments.getContent()));
        GetProductsResponse getProductsResponse = GetProductsResponse.fromSearch(productDocuments, representativeImages);
        httpSession.setAttribute(GET_PRODUCTS_RESPONSE, getProductsResponse);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/region")
    public ResponseEntity<Void> searchByRegionNameKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = ZERO) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            HttpSession httpSession
    ) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<ProductDocument> productDocuments = productSearchService.getByRegionKeyword(keyword, pageable);
        List<Image> representativeImages = imageService
                .getRepresentativeImages(RepresentativeImagesRequest.fromSearch(productDocuments.getContent()));
        GetProductsResponse getProductsResponse
                = GetProductsResponse.fromSearch(productDocuments, representativeImages);
        httpSession.setAttribute(GET_PRODUCTS_RESPONSE, getProductsResponse);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/school")
    public ResponseEntity<Void> searchBySchoolNameKeyword(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = ZERO) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            HttpSession httpSession
    ) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<ProductDocument> productDocuments = productSearchService.getBySchoolKeyword(keyword, pageable);
        List<Image> representativeImages = imageService
                .getRepresentativeImages(RepresentativeImagesRequest.fromSearch(productDocuments.getContent()));
        GetProductsResponse getProductsResponse
                = GetProductsResponse.fromSearch(productDocuments, representativeImages);
        httpSession.setAttribute(GET_PRODUCTS_RESPONSE, getProductsResponse);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/name/auto-complete")
    public ResponseEntity<List<String>> searchByAutoCompleteName(@RequestParam("keyword") String keyword) {
        return ResponseEntity.status(OK).body(
                productSearchService.getByStartWithNameKeyword(keyword)
                        .stream()
                        .map(ProductDocument::getName)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/title/auto-complete")
    public ResponseEntity<List<String>> searchByAutoComTitle(@RequestParam("keyword") String keyword) {
        return ResponseEntity.status(OK).body(
                productSearchService.getByStartWithTitleKeyword(keyword)
                        .stream()
                        .map(ProductDocument::getTitle)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/category/auto-complete")
    public ResponseEntity<List<String>> searchByAutoCompleteCategory(@RequestParam("keyword") String keyword) {
        return ResponseEntity.status(OK).body(
                productSearchService.getByStartWithCategoryKeyword(keyword)
                        .stream()
                        .map(ProductDocument::getThirdCategoryName)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/region/auto-complete")
    public ResponseEntity<List<String>> searchByAutoCompleteRegion(@RequestParam("keyword") String keyword) {
        return ResponseEntity.status(OK).body(
                productSearchService.getByStartWithRegionKeyword(keyword)
                        .stream()
                        .map(AddressModifier::joinAddress)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/school/auto-complete")
    public ResponseEntity<List<String>> searchByAutoCompleteSchool(@RequestParam("keyword") String keyword) {
        return ResponseEntity.status(OK).body(
                productSearchService.getByStartWithSchoolKeyword(keyword)
                        .stream()
                        .map(ProductDocument::getSchoolName)
                        .collect(Collectors.toList())
        );
    }
}
