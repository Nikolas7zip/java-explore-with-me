package ru.practicum.ewm.category;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.pagination.EntityPagination;

import java.util.List;

public interface CategoryService {
    CategoryDto get(Long id);

    List<CategoryDto> getAll(EntityPagination pagination);

    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto);

    void delete(Long id);
}
