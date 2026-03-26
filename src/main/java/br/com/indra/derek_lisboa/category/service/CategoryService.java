package br.com.indra.derek_lisboa.category.service;

import br.com.indra.derek_lisboa.exception.CategoryNotFoundException;
import br.com.indra.derek_lisboa.exception.InvalidCategoryNameException;
import br.com.indra.derek_lisboa.category.model.Category;
import br.com.indra.derek_lisboa.category.repository.CategoryRepository;
import br.com.indra.derek_lisboa.category.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryDTO save(CategoryDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidCategoryNameException("Nome da categoria é obrigatório");
        }

        Category category;

        if (dto.getId() != null) {
            category = repository.findById(dto.getId())
                    .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));

            if (!category.getName().equals(dto.getName()) && repository.existsByName(dto.getName())) {
                throw new InvalidCategoryNameException("Já existe uma categoria com esse nome");
            }

            category.setName(dto.getName());
        } else {
            if (repository.existsByName(dto.getName())) {
                throw new InvalidCategoryNameException("Já existe uma categoria com esse nome");
            }

            category = new Category();
            category.setName(dto.getName());
        }

        Category saved = repository.save(category);
        return toDTO(saved);
    }

    public List<CategoryDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}