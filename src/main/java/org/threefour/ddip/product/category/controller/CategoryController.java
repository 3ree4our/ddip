package org.threefour.ddip.product.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.GetCategoryResponse;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.util.FormatConverter;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/register-category")
    public ResponseEntity<Void> registerCategory(RegisterCategoryRequest registerCategoryRequest) {
        categoryService.createCategory(registerCategoryRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/get-category")
    public ResponseEntity<GetCategoryResponse> getCategory(String id) {
        Category category = categoryService.getCategory(FormatConverter.parseToShort(id));

        return ResponseEntity.status(OK).body(GetCategoryResponse.from(category));
    }
}
