package br.com.indra.derek_lisboa.user.controller;

import br.com.indra.derek_lisboa.user.dto.UserDTO;
import br.com.indra.derek_lisboa.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna uma lista com todos os usuarios cadastrados")
    public ResponseEntity<List<UserDTO>> getAll(){

        return ResponseEntity.ok(userService.getAll());
    }
}
