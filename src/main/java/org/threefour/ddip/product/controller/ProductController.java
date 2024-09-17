package org.threefour.ddip.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.image.domain.Image;
import org.threefour.ddip.image.domain.RepresentativeImagesRequest;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.product.domain.GetProductResponse;
import org.threefour.ddip.product.domain.GetProductsResponse;
import org.threefour.ddip.product.domain.Product;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.threefour.ddip.image.domain.TargetType.PRODUCT;
import static org.threefour.ddip.util.PagingConstant.*;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @GetMapping("/registration-form")
    public ModelAndView getRegistrationForm() {
        List<Category> categories = categoryService.getCategories(null);

        return new ModelAndView("product/registration", "categories", GetCategoriesResponse.from(categories));
    }

    @PostMapping("/registration-confirm")
    public String registerProduct(
            @ModelAttribute RegisterProductRequest registerProductRequest,
            @RequestParam("images") List<MultipartFile> images
    ) {
        Long productId = productService.createProduct(registerProductRequest, images);
        return String.format("redirect:details?id=%d", productId);
    }


    @GetMapping("/list")
    public ModelAndView getProducts(
            @RequestParam(defaultValue = TRUE) String paged,
            @RequestParam(defaultValue = MINUS_ONE) String pageNumber,
            @RequestParam(defaultValue = NINE) String size,
            @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING) String sort,
            @RequestParam(defaultValue = ZERO) String categoryId,
            HttpSession httpSession
    ) {
        if (pageNumber.equals(MINUS_ONE)) {
            httpSession.setAttribute("categoryId", categoryId);
            pageNumber = ZERO;
        }
        if (categoryId.equals(ZERO)) {
            categoryId = (String) httpSession.getAttribute("categoryId");
        }
        if (FormatValidator.isNoValue(categoryId)) {
            categoryId = ZERO;
        }

        Pageable pageable = FormatConverter.parseToBoolean(paged)
                ? PageRequest.of(
                FormatConverter.parseToInt(pageNumber),
                FormatConverter.parseToInt(size),
                FormatConverter.parseSortString(sort)
        )
                : Pageable.unpaged();

        Page<Product> products
                = productService.getProducts(pageable, FormatConverter.parseToShort(categoryId));
        List<Image> representativeImages
                = imageService.getRepresentativeImages(RepresentativeImagesRequest.from(products.getContent()));

        return new ModelAndView("product/list", "products", GetProductsResponse.from(products, representativeImages));
    }

    @GetMapping("/details")
    public ModelAndView getProduct(@RequestParam String id) {
        if (FormatValidator.isNoValue(id) || !FormatValidator.isNumberPattern(id)) {
            return new ModelAndView("redirect:list");
        }
        Long parsedId = FormatConverter.parseToLong(id);

        return new ModelAndView(
                "product/details", "product",
                GetProductResponse.from(productService.getProduct(parsedId), imageService.getImages(PRODUCT, parsedId))
        );
    }
}
