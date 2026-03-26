package br.com.indra.derek_lisboa.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Schema(description = "Dados para autenticação do usuário")
public record LoginDTO(

        @Schema(description = "Email do usuario", example = "teste@exemplo.com")
        @NotBlank(message = "O email é obrigatorio")
        @Email(message = "Email invalido")
        String email,

        @Schema(description = "Senha do usuario", example = "0123456789@")
        @NotBlank(message = "A senha é obrigatoria")
        String password

) {}
