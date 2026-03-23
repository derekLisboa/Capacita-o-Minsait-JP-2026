package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.service.ProductService;
import br.com.indra.derek_lisboa.service.dto.ProductHistoryDTO;
import br.com.indra.derek_lisboa.service.dto.ProductDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto){
        ProductDTO created = productService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO dto
    ){
        ProductDTO updated = productService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductDTO> updatePrice(
            @PathVariable UUID id,
            @RequestParam BigDecimal price
    ){
        ProductDTO updated = productService.updatePrice(id, price);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/price-history")
    public ResponseEntity<List<ProductHistoryDTO>> getPriceHistory(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getPriceHistory(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category
    ){
        List<ProductDTO> results;

        if (name != null && category != null) {
            results = productService.searchByNameAndCategory(name, category);
        } else if (name != null) {
            results = productService.searchByName(name);
        } else if (category != null) {
            results = productService.searchByCategory(category);
        } else {
            results = productService.getAll();
        }

        return ResponseEntity.ok(results);
    }
}