package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.CategoryNotFoundException;
import br.com.indra.derek_lisboa.exception.InvalidCategoryNameException;
import br.com.indra.derek_lisboa.model.Category;
import br.com.indra.derek_lisboa.repository.CategoryRepository;
import br.com.indra.derek_lisboa.service.dto.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository repository;

    @Test
    void shouldCreateCategorySuccessfully() {

        CategoryDTO dto = new CategoryDTO();
        dto.setName("SSD");

        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("SSD");

        when(repository.existsByName("SSD")).thenReturn(false);
        when(repository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.save(dto);

        assertNotNull(result);
        assertEquals("SSD", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {

        CategoryDTO dto = new CategoryDTO();
        dto.setName("");

        assertThrows(InvalidCategoryNameException.class, () -> {
            categoryService.save(dto);
        });
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExistsOnCreate() {

        CategoryDTO dto = new CategoryDTO();
        dto.setName("SSD");

        when(repository.existsByName("SSD")).thenReturn(true);

        assertThrows(InvalidCategoryNameException.class, () -> {
            categoryService.save(dto);
        });
    }

    @Test
    void shouldUpdateCategorySuccessfully() {

        UUID id = UUID.randomUUID();

        CategoryDTO dto = new CategoryDTO();
        dto.setId(id);
        dto.setName("SSD NVME");

        Category category = new Category();
        category.setId(id);
        category.setName("SSD");

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByName("SSD NVME")).thenReturn(false);
        when(repository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        CategoryDTO result = categoryService.save(dto);

        assertEquals("SSD NVME", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundOnUpdate() {

        UUID id = UUID.randomUUID();

        CategoryDTO dto = new CategoryDTO();
        dto.setId(id);
        dto.setName("SSD");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.save(dto);
        });
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExistsOnUpdate() {

        UUID id = UUID.randomUUID();

        CategoryDTO dto = new CategoryDTO();
        dto.setId(id);
        dto.setName("SSD");

        Category category = new Category();
        category.setId(id);
        category.setName("HD");

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByName("SSD")).thenReturn(true);

        assertThrows(InvalidCategoryNameException.class, () -> {
            categoryService.save(dto);
        });
    }

    @Test
    void shouldReturnAllCategories() {

        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("SSD");

        when(repository.findAll()).thenReturn(List.of(category));

        List<CategoryDTO> result = categoryService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnCategoryById() {
        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);
        category.setName("SSD");

        when(repository.findById(id)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.findById(id);

        assertEquals("SSD", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.findById(id);
        });
    }

    @Test
    void shouldDeleteCategorySuccessfully() {

        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id);

        verify(repository).delete(category);
    }

    @Test
    void shouldThrowExceptionWhenDeleteCategoryNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.delete(id);
        });

    }
}