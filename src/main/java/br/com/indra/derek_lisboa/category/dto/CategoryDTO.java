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

        UUID id,

        @NotBlank(message = "O nome da categoria é obrigatorio")
        String name,

        UUID parentId

) {}
