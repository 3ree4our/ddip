package org.threefour.ddip.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.image.service.ImageService;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.product.domain.GetProductResponse;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.service.ProductService;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import java.util.List;

import static org.threefour.ddip.image.domain.TargetType.PRODUCT;

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
