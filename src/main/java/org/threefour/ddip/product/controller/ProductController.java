package org.threefour.ddip.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

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
}
