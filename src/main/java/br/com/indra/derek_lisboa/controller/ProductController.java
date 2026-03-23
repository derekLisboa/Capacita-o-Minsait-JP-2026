package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.model.Product;
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
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll(){
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductDTO dto
    ){
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Product> updatePrice(
            @PathVariable UUID id,
            @RequestParam BigDecimal price
    ){
        return ResponseEntity.ok(productService.updatePrice(id, price));
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
}