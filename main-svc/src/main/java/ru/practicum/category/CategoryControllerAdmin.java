package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;


@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryControllerAdmin {
    private final CategoryService categoryService;
    private final CategoryMapper categoriesMapper;

    @PostMapping
    public ResponseEntity<CategoryDto> postCategory(@Valid @RequestBody NewCategoryDto newCategory) {
        log.info("Post new category {}", newCategory);
        return new ResponseEntity<>(categoriesMapper.convertCategoryToDto(categoryService.postCategory(newCategory)), HttpStatus.CREATED);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patchCategory(@Valid @RequestBody NewCategoryDto newCategory,
                                                     @Positive @PathVariable Long catId) {
        log.info("Patch category {} Id={}", newCategory, catId);
        return new ResponseEntity<>(categoriesMapper.convertCategoryToDto(categoryService.patchCategory(newCategory, catId)), HttpStatus.OK);
    }


    @DeleteMapping("/{catId}")
    public ResponseEntity<HttpStatus> deleteCategory(@Positive @PathVariable Long catId) {
        log.info("Delete сategory with id={}", catId);
        categoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
