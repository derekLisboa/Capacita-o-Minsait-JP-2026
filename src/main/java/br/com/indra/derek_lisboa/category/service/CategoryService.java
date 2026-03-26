package br.com.indra.derek_lisboa.category.service;

import br.com.indra.derek_lisboa.exception.CategoryNotFoundException;
import br.com.indra.derek_lisboa.exception.InvalidCategoryNameException;
import br.com.indra.derek_lisboa.category.model.Category;
import br.com.indra.derek_lisboa.category.repository.CategoryRepository;
import br.com.indra.derek_lisboa.category.dto.CategoryDTO;
import br.com.indra.derek_lisboa.product.dto.ProductDTO;
import br.com.indra.derek_lisboa.product.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryDTO create(CategoryDTO dto) {

        if (repository.existsByName(dto.name())) {
            throw new InvalidCategoryNameException("Já existe uma categoria com esse nome");
        }

        Category category = new Category();
        category.setName(dto.name());

        return toDTO(repository.save(category));
    }

    public CategoryDTO update(UUID id, CategoryDTO dto) {

        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));

        if (!category.getName().equals(dto.name()) &&
                repository.existsByName(dto.name())) {

            throw new InvalidCategoryNameException("Já existe uma categoria com esse nome");
        }

        category.setName(dto.name());

        return toDTO(repository.save(category));
    }

    public List<CategoryDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public CategoryDTO findById(UUID id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));
        return toDTO(category);
    }

    public void delete(UUID id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));
        repository.delete(category);
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}