package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.model.Cart;
import br.com.indra.derek_lisboa.service.CartService;
import br.com.indra.derek_lisboa.service.dto.CartDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Carrinho", description = "Gerenciamento do carrinho de compras")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @Operation(summary = "Adicionar produto no carrinho",
            description = "Adiciona um produto ao carrinho do usuário")

    public ResponseEntity<CartDTO> addProduct(

            @Parameter(description = "Email do usuario", example = "derek@teste.com")
            @RequestParam String email,

            @Parameter(description = "ID do produto", example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28")
            @RequestParam UUID productId,

            @Parameter(description = "Quantidade do produto", example = "2")
            @RequestParam Integer quantity
    ) {

        Cart cart = cartService.addProduct(email, productId, quantity);

        CartDTO dto = cartService.toDTO(cart);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Buscar carrinho",
            description = "Retorna o carrinho do usuario com todos os itens e valor total"
    )
    public ResponseEntity<CartDTO> getCart(
            @Parameter(description = "Email do usuario", example = "derek@teste.com")
            @RequestParam String email
    ) {
        Cart cart = cartService.getOrCreateCart(email);
        return ResponseEntity.ok(cartService.toDTO(cart));
    }

    @DeleteMapping("/remove")
    @Operation(summary = "Remover produto do carrinho",
            description = "Remove uma quantidade especifica do produto. Se for maior ou igual a quantidade atual, remove o item completamente")
    public ResponseEntity<CartDTO> removeProduct(

            @Parameter(description = "Email do usuario", example = "derek@teste.com")
            @RequestParam String email,

            @Parameter(description = "ID do produto", example = "04b7ee82-c5ca-427e-8f30-6d50662c9e28")
            @RequestParam UUID productId,

            @Parameter(description = "Quantidade para remover", example = "1")
            @RequestParam Integer quantity
    ) {

        Cart cart = cartService.removeProduct(email, productId, quantity);

        return ResponseEntity.ok(cartService.toDTO(cart));
    }
}
