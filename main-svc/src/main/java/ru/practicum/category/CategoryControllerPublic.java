package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryControllerPublic {
    private final CategoryService categoryService;
    private final CategoryMapper categoriesMapper;


    @GetMapping
    public ResponseEntity<Collection<CategoryDto>> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get list categories from={}, size={}", from, size);
        return new ResponseEntity<>(categoriesMapper.convertCollCategoryToDto(categoryService.getCategories(from, size)), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategories(@Positive @PathVariable Long catId) {
        log.info("Get category by ID={}", catId);
        return new ResponseEntity<>(categoriesMapper.convertCategoryToDto(categoryService.getCategoryById(catId)), HttpStatus.OK);
    }


}
