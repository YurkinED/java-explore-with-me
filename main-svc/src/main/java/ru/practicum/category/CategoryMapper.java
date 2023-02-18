package ru.practicum.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto convertCategoryToDto(Category category);

    Collection<CategoryDto> convertCollCategoryToDto(Page<Category> category);

    @Mapping(target = "id", ignore = true)
    Category convertNewCategoryDtoToCategory(NewCategoryDto newCategoryDto);

    @Mapping(target = "id", ignore = true)
    void updateCategoryFromNewCategoryDto(NewCategoryDto newCategoryDto, @MappingTarget Category category);
}
