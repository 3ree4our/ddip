package org.threefour.ddip.product.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.domain.RegisterCategoryRequest;
import org.threefour.ddip.product.category.exception.CategoryNotFoundException;
import org.threefour.ddip.product.category.repository.CategoryRepository;
import org.threefour.ddip.util.FormatConverter;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.*;
import static org.threefour.ddip.product.category.exception.ExceptionMessage.CATEGORY_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(isolation = SERIALIZABLE, timeout = 30)
    public void createCategory(RegisterCategoryRequest registerCategoryRequest) {
        try {
            Category parentCategory
                    = getCategory(FormatConverter.parseToShort(registerCategoryRequest.getParentCategoryId()));
            categoryRepository.save(Category.from(registerCategoryRequest, parentCategory));
        } catch (CategoryNotFoundException cnfe) {
            categoryRepository.save(Category.from(registerCategoryRequest));
        }
    }

    @Override
    @Transactional(isolation = READ_COMMITTED, readOnly = true, timeout = 20)
    public List<Category> getCategories(Short parentCategoryId) {
        return categoryRepository.findByParentCategoryId(parentCategoryId);
    }

    @Override
    @Transactional(isolation = READ_UNCOMMITTED, readOnly = true, timeout = 10)
    public Category getCategory(Short categoryId) {
        return categoryRepository.findByIdAndDeleteYnFalse(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_EXCEPTION_MESSAGE, categoryId))
        );
    }

    @Override
    public void deleteCategory(short id) {
        Category category = getCategory(id);
        category.delete();
        categoryRepository.save(category);
    }
}
