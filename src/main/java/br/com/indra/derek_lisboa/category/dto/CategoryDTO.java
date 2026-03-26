package br.com.indra.derek_lisboa.category.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Schema(description = "Categoria do produto")
public record CategoryDTO(

    @Schema(description = "ID da categoria", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    UUID id,

    @NotBlank(message = "O nome da categoria é obrigatoio")
    @Schema(description = "Nome da categoria", example = "SSD")
    String name)

{}
