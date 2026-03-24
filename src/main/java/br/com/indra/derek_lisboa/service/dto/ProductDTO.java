package br.com.indra.derek_lisboa.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Produto")
public class ProductDTO {

    @Schema(description = "ID do produto", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @NotBlank
    @Schema(description = "Nome do produto", example = "Memória RAM 32Gb")
    private String name;

    @NotBlank
    @Schema(description = "Marca do produto", example = "HyperX")
    private String brand;

    @NotNull
    @Positive
    @Schema(description = "Preço do produto", example = "5999.99")
    private BigDecimal price;

    @Schema(description = "Código de barras do produto", example = "1234567890123")
    private String barCode;

    @NotNull
    @Schema(description = "ID da categoria do produto", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID categoryId;

}