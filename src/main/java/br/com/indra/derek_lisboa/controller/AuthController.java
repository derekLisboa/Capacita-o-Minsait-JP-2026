package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.exception.InvalidPasswordException;
import br.com.indra.derek_lisboa.model.User;
import br.com.indra.derek_lisboa.security.JWTUtil;
import br.com.indra.derek_lisboa.service.UserService;
import br.com.indra.derek_lisboa.dto.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticaçao", description = "Login de usuarios")
public class AuthController {

    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autenticaçao do usuario para retornar o token JWT")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDTO dto) {

        var user = userService.findByEmail(dto.email());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Senha inválida");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuario", description = "Criar um novo usuario no sistema")
    public ResponseEntity<String> register(@RequestBody @Valid User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Usuario cadastrado com sucesso");
    }
}