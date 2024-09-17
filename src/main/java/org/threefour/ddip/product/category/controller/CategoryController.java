package org.threefour.ddip.product.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.GetCategoriesResponse;
import org.threefour.ddip.product.category.domain.GetCategoryResponse;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;
import org.threefour.ddip.product.category.service.CategoryService;
import org.threefour.ddip.util.FormatConverter;
import org.threefour.ddip.util.FormatValidator;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/register")
    public ResponseEntity<Void> registerCategory(RegisterCategoryRequest registerCategoryRequest) {
        categoryService.createCategory(registerCategoryRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/list")
    public ResponseEntity<GetCategoriesResponse> getCategories(String parentCategoryId) {
        Short parsedParentCategoryId = null;
        if (!FormatValidator.isNoValue(parentCategoryId) && FormatValidator.isNumberPattern(parentCategoryId)) {
            parsedParentCategoryId = FormatConverter.parseToShort(parentCategoryId);
        }

        List<Category> categories = categoryService.getCategories(parsedParentCategoryId);
        GetCategoriesResponse getCategoriesResponse = GetCategoriesResponse.from(categories);

        return ResponseEntity.status(OK).body(getCategoriesResponse);
    }

    @GetMapping("/details")
    public ResponseEntity<GetCategoryResponse> getCategory(String id) {
        Category category = categoryService.getCategory(FormatConverter.parseToShort(id));

        return ResponseEntity.status(OK).body(GetCategoryResponse.from(category));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCategory(String id) {
        categoryService.deleteCategory(FormatConverter.parseToShort(id));

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
