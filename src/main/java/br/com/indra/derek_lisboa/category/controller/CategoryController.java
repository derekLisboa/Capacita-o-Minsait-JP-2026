package br.com.indra.derek_lisboa.category.controller;

import br.com.indra.derek_lisboa.category.service.CategoryService;
import br.com.indra.derek_lisboa.category.dto.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints de gerenciamento de categorias")
public class CategoryController {

    private final CategoryService categoryService;


    @PostMapping
    @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria")
    public ResponseEntity<CategoryDTO> create(@RequestBody CategoryDTO dto) {
        CategoryDTO created = categoryService.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    @Operation(summary = "Buscar categorias", description = "Retorna uma lista com todas as categorias cadastradas")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria pelo ID")
    public ResponseEntity<CategoryDTO> getById(@PathVariable UUID id) {
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria por ID", description = "Atualiza os dados de uma categoria existente pelo ID")
    public ResponseEntity<CategoryDTO> update(@PathVariable UUID id, @RequestBody CategoryDTO dto) {
        dto.setId(id);
        CategoryDTO updated = categoryService.save(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria por ID", description = "Deleta uma categoria pelo seu ID")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
