package br.com.indra.derek_lisboa.service.dto;

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
public class ProductDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String brand;

    @NotNull
    @Positive
    private BigDecimal price;

    private String barCode;

    @NotNull
    private UUID categoryId;

}