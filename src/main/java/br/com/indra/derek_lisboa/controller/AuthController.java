package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.exception.InvalidPasswordException;
import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.model.User;
import br.com.indra.derek_lisboa.repository.UserRepository;
import br.com.indra.derek_lisboa.security.JWTUtil;
import br.com.indra.derek_lisboa.service.UserService;
import br.com.indra.derek_lisboa.service.dto.LoginDTO;
import br.com.indra.derek_lisboa.service.dto.ProductDTO;
import br.com.indra.derek_lisboa.service.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login de usuarios")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autenticação do usuario para retornar o token JWT")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO dto) {

        var user = userService.findByEmail(dto.getEmail());

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new InvalidPasswordException("Senha invalida");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuario", description = "Criar um novo usuario no sistema")
    public ResponseEntity<String> register(@RequestBody User user) {

        userService.save(user);

        return ResponseEntity.ok("Usuario cadastrado com sucesso");
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna uma lista com todos os usuarios cadastrados")
    public ResponseEntity<List<UserDTO>> getAll(){

        return ResponseEntity.ok(userService.getAll());
    }
}