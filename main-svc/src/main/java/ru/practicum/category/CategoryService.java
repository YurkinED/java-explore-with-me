package ru.practicum.category;

import org.springframework.data.domain.Page;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;



public interface CategoryService {
    Page<Category> getCategories(Integer from, Integer size);

    Category getCategoryById(Long catId);

    Category postCategory(NewCategoryDto newCategory);

    Category patchCategory(NewCategoryDto newCategory, Long catId);

    void deleteCategory(Long catId);
}
