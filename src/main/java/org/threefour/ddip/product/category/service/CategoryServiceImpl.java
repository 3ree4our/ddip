package org.threefour.ddip.product.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.threefour.ddip.product.category.domain.Category;
import org.threefour.ddip.product.category.exception.CategoryNotFoundException;
import org.threefour.ddip.product.category.repository.CategoryRepository;

import static org.springframework.transaction.annotation.Isolation.READ_UNCOMMITTED;
import static org.threefour.ddip.product.category.exception.ExceptionMessage.CATEGORY_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(isolation = READ_UNCOMMITTED, readOnly = true, timeout = 10)
    public Category getCategory(Short categoryId) {
        return categoryRepository.findByIdAndDeleteYnFalse(categoryId).orElseThrow(
                () -> new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_EXCEPTION_MESSAGE, categoryId))
        );
    }
}
