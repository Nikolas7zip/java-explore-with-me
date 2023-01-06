package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category mapToNewCategory(NewCategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());

        return category;
    }

    public static Category mapToCategory(CategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());

        return category;
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> mapToCategoryDto(List<Category> categories) {
        List<CategoryDto> dtos = new ArrayList<>();
        for (Category category : categories) {
            dtos.add(mapToCategoryDto(category));
        }

        return dtos;
    }
}
