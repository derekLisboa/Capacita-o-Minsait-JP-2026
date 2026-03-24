package br.com.indra.derek_lisboa.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaçoes")
public class InventoryTransactionDTO {

    @Schema(description = "ID da transaçao", example = "a1b2c3d4-e5f6-7890-abcd-123456789000")
    private UUID id;

    @Schema(description = "ID do produto", example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28")
    private UUID productId;

    @Schema(description = "Nome do produto", example = "SSD 1Tb")
    private String productName;

    @Schema(description = "Quantidade movimentada", example = "2")
    private Integer quantity;

    @Schema(description = "Tipo da movimentaçao (ENTRY = entrada, EXIT = saida)", example = "EXIT")
    private String type;

    @Schema(description = "Data da movimentação", example = "2026-03-24T13:45:00")
    private LocalDateTime createdAt;
}