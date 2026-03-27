package br.com.indra.derek_lisboa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(name = "Carrinho de compras", description = "Carrinho de compras do usuario")
public record CartDTO(

        @Schema(description = "ID do carrinho de compras", example = "6b4234b3-3020-46d5-b942-a7e8536356fc")
        UUID id,

        @Schema(description = "Email do usuario dono do carrinho", example = "derek@teste.com")
        @NotBlank(message = "Email do usuário é obrigatório")
        String userEmail,

        @Schema(description = "Lista de itens do carrinho de compras")
        List<CartItemDTO> items,

        @Schema(description = "Valor total do carrinho de compras", example = "13000.00")
        @NotNull(message = "Total é obrigatório")
        @PositiveOrZero(message = "Total deve ser maior ou igual a zero")
        BigDecimal total

) {}
