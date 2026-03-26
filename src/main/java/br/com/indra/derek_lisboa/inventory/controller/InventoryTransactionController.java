package br.com.indra.derek_lisboa.inventory.controller;

import br.com.indra.derek_lisboa.inventory.service.InventoryTransactionService;
import br.com.indra.derek_lisboa.inventory.dto.InventoryTransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Histórico de movimentações de estoque")
public class InventoryTransactionController {

    private final InventoryTransactionService service;

    @GetMapping
    @Operation(summary = "Listar movimentaçoes",
            description = "Retorna o histórico de entradas e saidas de estoque"
    )
    public ResponseEntity<List<InventoryTransactionDTO>> findAll() {

        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Buscar movimentações por produto",
            description = "Retorna o histórico de movimentações de um produto específico"
    )
    public ResponseEntity<List<InventoryTransactionDTO>> findByProduct(

            @Parameter(description = "ID do produto",
                    example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28"
            )
            @PathVariable UUID productId
    ) {
        return ResponseEntity.ok(service.findByProduct(productId));
    }
}
