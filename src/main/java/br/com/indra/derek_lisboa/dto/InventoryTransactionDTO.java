package br.com.indra.derek_lisboa.dto;

import br.com.indra.derek_lisboa.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Transaçoes")
public record InventoryTransactionDTO(

        @Schema(description = "ID da transaçao", example = "a1b2c3d4-e5f6-7890-abcd-123456789000")
        UUID id,

        @Schema(description = "ID do produto", example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28")
        @NotNull(message = "O ID do produto é obrigatorio")
        UUID productId,

        @Schema(description = "Nome do produto", example = "SSD 1Tb")
        String productName,

        @Schema(description = "Quantidade movimentada", example = "2")
        @NotNull(message = "A quantidade é obrigatoria")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantity,

        @Schema(description = "Tipo da movimentação (ENTRY = entrada, EXIT = saída)", example = "EXIT")
        @NotNull(message = "O tipo é obrigatorio")
        TransactionType type,

        @Schema(description = "Data da movimentaçao", example = "2026-03-24T13:45:00")
        LocalDateTime createdAt

) {
}