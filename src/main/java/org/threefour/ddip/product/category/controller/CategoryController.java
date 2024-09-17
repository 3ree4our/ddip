package org.threefour.ddip.product.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<Void> registerCategory(RegisterCategoryRequest registerCategoryRequest) {
        categoryService.createCategory(registerCategoryRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping()
    public ResponseEntity<GetCategoriesResponse> getCategories(@RequestParam("parentCategoryId") String parentCategoryId) {
        Short parsedParentCategoryId = null;
        if (!FormatValidator.isNoValue(parentCategoryId) && FormatValidator.isNumberPattern(parentCategoryId)) {
            parsedParentCategoryId = FormatConverter.parseToShort(parentCategoryId);
        }

        List<Category> categories = categoryService.getCategories(parsedParentCategoryId);
        GetCategoriesResponse getCategoriesResponse = GetCategoriesResponse.from(categories);

        return ResponseEntity.status(OK).body(getCategoriesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable("id") String id) {
        Category category = categoryService.getCategory(FormatConverter.parseToShort(id));

        return ResponseEntity.status(OK).body(GetCategoryResponse.from(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") String id) {
        categoryService.deleteCategory(FormatConverter.parseToShort(id));

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
