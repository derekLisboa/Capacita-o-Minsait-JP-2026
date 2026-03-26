package br.com.indra.derek_lisboa.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "Item do carrinho de compras", description = "Item do carrinho de compras")
public record CartItemDTO(

        @Schema(description = "ID do produto", example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28")
        @NotNull(message = "O ID do produto é obrigatorio")
        UUID productId,

        @Schema(description = "Nome do produto", example = "Memória RAM XXX")
        @NotBlank(message = "O nome do produto é obrigatorio")
        String productName,

        @Schema(description = "Marca do produto", example = "HyperX")
        @NotBlank(message = "A marca do produto é obrigatoria")
        String brand,

        @Schema(description = "Preço do produto", example = "6500.0")
        @NotNull(message = "O preço é obrigatorio")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal price,

        @Schema(description = "Quantidade do produto no carrinho", example = "2")
        @NotNull(message = "A quantidade é obrigatoria")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity

) {}
