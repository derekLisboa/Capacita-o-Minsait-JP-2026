package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.dto.ProductHistoryDTO;
import br.com.indra.derek_lisboa.service.PriceHistoryService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/price-history")
@RequiredArgsConstructor
@Tag(name = "Historico de Preços", description = "Consulta de alteraçoes de preços do produto")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @GetMapping("/product/{productId}")
    @Operation(
            summary = "Buscar historico de preços por produto",
            description = "Retorna todas as alteraçoes de preço de um produto especifico, ordenadas da mais recente para a mais antiga"
    )
    public ResponseEntity<List<ProductHistoryDTO>> getByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(priceHistoryService.getHistoryByProductId(productId));
    }
}
