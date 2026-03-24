package br.com.indra.derek_lisboa.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Carrinho de compras", description = "Carrinho de compras do usuario")
public class CartDTO {

    @Schema(description = "ID do carrinho de compras", example = "6b4234b3-3020-46d5-b942-a7e8536356fc")
    private UUID id;

    @Schema(description = "Email do usuario dono do carrinho", example = "derek@teste.com")
    private String userEmail;

    @Schema(description = "Lista de itens do carrinho de compras")
    private List<CartItemDTO> items;

    @Schema(description = "Valor total do carrinho de compras", example = "13000.00")
    private BigDecimal total;
}
