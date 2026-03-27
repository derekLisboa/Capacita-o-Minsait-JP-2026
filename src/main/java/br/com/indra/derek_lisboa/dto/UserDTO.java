package br.com.indra.derek_lisboa.dto;

import br.com.indra.derek_lisboa.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados do usuário")
public record UserDTO(

        @Schema(description = "Email do usuario", example = "teste@exemplo.com")
        @NotBlank(message = "O email é obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @Schema(description = "Cargo do usuário", example = "USER")
        @NotNull(message = "O cargo é obrigatorio")
        UserRole role

) {}
