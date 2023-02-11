package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoriesMapper;

    public Page<Category> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of((from / size), size);
        return categoryRepository.findAll(page);
    }

    public Category getCategoryById(Long catId) {
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This category not found");
        }
    }

    public Category postCategory(NewCategoryDto newCategory) {
        try {
            return categoryRepository.save(categoriesMapper.convertNewCategoryDtoToCategory(newCategory));
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This category name is duplicated", exception);
        }
    }

    public Category patchCategory(NewCategoryDto newCategory, Long catId) {
        Category category = getCategoryById(catId);
        categoriesMapper.updateCategoryFromNewCategoryDto(newCategory, category);
        try {
            return categoryRepository.save(category);
        } catch (Exception error) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, error.getMessage());
        }
    }

    public void deleteCategory(Long catId) {
        try {
            categoryRepository.deleteById(catId);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This category isn't empty", exception);
        }
    }
}
