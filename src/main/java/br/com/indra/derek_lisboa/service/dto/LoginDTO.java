package br.com.indra.derek_lisboa.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login")
public class LoginDTO {

    @Schema(description = "Email do usuario", example = "teste@exemplo.com")
    @NotBlank(message = "O email é obrigatorio")
    private String email;

    @Schema(description = "Senha do usuario", example = "0123456789@")
    @NotBlank(message = "A senha é obrigatoria")
    private String password;

}
