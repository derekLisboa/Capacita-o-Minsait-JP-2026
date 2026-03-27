package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.CategoryDeletionException;
import br.com.indra.derek_lisboa.exception.CategoryNotFoundException;
import br.com.indra.derek_lisboa.exception.InvalidCategoryNameException;
import br.com.indra.derek_lisboa.model.Category;
import br.com.indra.derek_lisboa.repository.CategoryRepository;
import br.com.indra.derek_lisboa.dto.CategoryDTO;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final ProductRepository productRepository;

    private Category resolveParent(UUID parentId) {
        if (parentId == null) {
            return null;
        }

        return repository.findById(parentId)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Categoria pai nao encontrada"));
    }

    private void validateDuplicate(Category category) {
        if (repository.existsByNameIgnoreCaseAndParent(
                category.getName(),
                category.getParent())) {

            throw new InvalidCategoryNameException("Ja existe categoria com esse nome neste nivel");
        }
    }

    public CategoryDTO create(CategoryDTO dto) {

        Category parent = resolveParent(dto.parentId());

        Category category = new Category();
        String name = dto.name().trim();
        category.setName(name);
        category.setParent(parent);

        validateDuplicate(category);

        return toDTO(repository.save(category));
    }

    public CategoryDTO update(UUID id, CategoryDTO dto) {

        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));

        Category parent = resolveParent(dto.parentId());

        if (parent != null && category.getId().equals(parent.getId())) {
            throw new InvalidCategoryNameException(
                    "Categoria nao pode ser pai dela mesma");
        }

        String newName = dto.name().trim();

        boolean changed =
                !category.getName().equalsIgnoreCase(newName) ||
                        !Objects.equals(category.getParent(), parent);

        if (changed &&
                repository.existsByNameIgnoreCaseAndParent(newName, parent)) {

            throw new InvalidCategoryNameException(
                    "Ja existe categoria com esse nome neste nivel");
        }



        category.setName(newName);
        category.setParent(parent);

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

        if (repository.existsByParent(category)) {
            throw new CategoryDeletionException(
                    "Nao é possivel deletar categoria que possui subcategorias");
        }
        if (productRepository.existsByCategory(category)) {
            throw new CategoryDeletionException(
                    "Nao é possivel deletar categoria com produtos associados");
        }

        repository.delete(category);
    }

    private CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null
        );
    }

}