package br.com.indra.derek_lisboa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDTO(

        UUID id,

        @NotBlank(message = "O nome é obrigatorio")
        String name,

        @NotBlank(message = "A marca é obrigatoria")
        String brand,

        @NotNull(message = "O preço é obrigatorio")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal price,

        String barCode,

        @NotNull(message = "O estoque é obrigatorio")
        @PositiveOrZero(message = "O estoque deve ser maior ou igual a 0")
        Integer stock,

        @NotNull(message = "A categoria é obrigatoria")
        UUID categoryId

) {}