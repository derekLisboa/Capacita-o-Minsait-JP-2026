package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.category.service.CategoryService;
import br.com.indra.derek_lisboa.exception.CategoryDeletionException;
import br.com.indra.derek_lisboa.exception.CategoryNotFoundException;
import br.com.indra.derek_lisboa.exception.InvalidCategoryNameException;
import br.com.indra.derek_lisboa.category.model.Category;
import br.com.indra.derek_lisboa.category.repository.CategoryRepository;
import br.com.indra.derek_lisboa.category.dto.CategoryDTO;
import br.com.indra.derek_lisboa.product.repository.ProductRepository;
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

    @Mock
    private ProductRepository productRepository;

    @Test
    void shouldCreateCategorySuccessfully() {

        CategoryDTO dto = new CategoryDTO(null, "SSD", null);

        when(repository.existsByNameIgnoreCaseAndParent("SSD", null)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> {
            Category saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        CategoryDTO result = categoryService.create(dto);

        assertNotNull(result);
        assertEquals("SSD", result.name());
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExistsOnCreate() {

        CategoryDTO dto = new CategoryDTO(null, "SSD", null);

        when(repository.existsByNameIgnoreCaseAndParent("SSD", null)).thenReturn(true);

        assertThrows(InvalidCategoryNameException.class,
                () -> categoryService.create(dto));
    }

    @Test
    void shouldCreateCategoryWithParentSuccessfully() {

        UUID parentId = UUID.randomUUID();

        Category parent = new Category();
        parent.setId(parentId);
        parent.setName("Hardware");

        CategoryDTO dto = new CategoryDTO(null, "SSD", parentId);

        when(repository.findById(parentId)).thenReturn(Optional.of(parent));
        when(repository.existsByNameIgnoreCaseAndParent("SSD", parent)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> {
            Category saved = i.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        CategoryDTO result = categoryService.create(dto);

        assertEquals("SSD", result.name());
        assertEquals(parentId, result.parentId());
    }

    @Test
    void shouldThrowExceptionWhenParentNotFound() {

        UUID parentId = UUID.randomUUID();

        CategoryDTO dto = new CategoryDTO(null, "SSD", parentId);

        when(repository.findById(parentId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.create(dto));
    }

    @Test
    void shouldUpdateCategorySuccessfully() {

        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);
        category.setName("SSD");

        CategoryDTO dto = new CategoryDTO(id, "SSD NVME", null);

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByNameIgnoreCaseAndParent("SSD NVME", null)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        CategoryDTO result = categoryService.update(id, dto);

        assertEquals("SSD NVME", result.name());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundOnUpdate() {

        UUID id = UUID.randomUUID();

        CategoryDTO dto = new CategoryDTO(id, "SSD", null);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.update(id, dto));
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExistsOnUpdate() {

        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);
        category.setName("HD");

        CategoryDTO dto = new CategoryDTO(id, "SSD", null);

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByNameIgnoreCaseAndParent("SSD", null)).thenReturn(true);

        assertThrows(InvalidCategoryNameException.class,
                () -> categoryService.update(id, dto));
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

        assertEquals("SSD", result.name());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.findById(id));
    }

    @Test
    void shouldDeleteCategorySuccessfully() {

        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByParent(category)).thenReturn(false);
        when(productRepository.existsByCategory(category)).thenReturn(false);

        categoryService.delete(id);

        verify(repository).delete(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryHasChildrenOnDelete() {

        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByParent(category)).thenReturn(true);

        assertThrows(CategoryDeletionException.class,
                () -> categoryService.delete(id));
    }

    @Test
    void shouldThrowExceptionWhenCategoryHasProductsOnDelete() {

        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.existsByParent(category)).thenReturn(false);
        when(productRepository.existsByCategory(category)).thenReturn(true);

        assertThrows(CategoryDeletionException.class,
                () -> categoryService.delete(id));
    }

    @Test
    void shouldThrowExceptionWhenDeleteCategoryNotFound() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.delete(id));
    }
}
