package br.com.indra.derek_lisboa.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Schema(description = "Email do usuario", example = "teste@exemplo.com")
    @NotBlank(message = "O email é obrigatorio")
    private String email;

    @Schema(description = "Cargo", example = "User, Admin")
    @NotBlank(message = "O cargo é obrigatorio")
    private String role;
}
