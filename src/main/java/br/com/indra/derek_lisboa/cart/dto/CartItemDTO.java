package br.com.indra.derek_lisboa.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Item do carrinho de compras", description = "item do carrinho de compras")
public class CartItemDTO {

    @Schema(description = "ID do produto", example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28")
    private UUID productId;

    @Schema(description = "Nome do produto", example = "Memória RAM XXX")
    private String productName;

    @Schema(description = "Marca do produto", example = "HyperK")
    private String brand;

    @Schema(description = "Preço do produto", example = "6500.0")
    private BigDecimal price;

    @Schema(description = "Quantidade do produto no carrinho", example = "2")
    private Integer quantity;
}
