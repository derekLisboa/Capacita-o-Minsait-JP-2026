package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.dto.UserDTO;
import br.com.indra.derek_lisboa.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gerenciamento de usuarios")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna uma lista com todos os usuarios cadastrados")
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
}
