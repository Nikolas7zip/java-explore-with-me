package ru.practicum.ewm.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.pagination.EntityPagination;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto get(Long id) {
        log.info("Get category with id=" + id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Category.class, id));

        return CategoryMapper.mapToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAll(EntityPagination pagination) {
        log.info("Get categories " + pagination);
        List<Category> categories = categoryRepository.findAll(pagination.getPageable()).getContent();

        return CategoryMapper.mapToCategoryDto(categories);
    }

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category createdCategory = categoryRepository.save(CategoryMapper.mapToNewCategory(newCategoryDto));
        log.info("Created " + createdCategory);

        return CategoryMapper.mapToCategoryDto(createdCategory);
    }

    @Transactional
    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        if (!categoryRepository.existsById(categoryDto.getId())) {
            throw new EntityNotFoundException(Category.class, categoryDto.getId());
        }
        Category updatedCategory = categoryRepository.save(CategoryMapper.mapToCategory(categoryDto));
        log.info("Updated " + updatedCategory);

        return CategoryMapper.mapToCategoryDto(updatedCategory);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        get(id);
        if (eventRepository.countByCategory_Id(id) != 0) {
            throw new BadRequestException("Some events refer to category");
        }
        categoryRepository.deleteById(id);
        log.info("Deleted category with id=" + id);
    }

}
