package org.threefour.ddip.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.threefour.ddip.product.domain.RegisterProductRequest;
import org.threefour.ddip.product.service.ProductService;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/registration-form")
    public String getRegistrationForm() {
        return "product/registration";
    }

    @PostMapping("/registration-confirm")
    public String registerProduct(RegisterProductRequest registerProductRequest) {
        Long productId = productService.createProduct(registerProductRequest);
        return String.format("redirect:/product/product-details?productId=%d", productId);
    }
}
