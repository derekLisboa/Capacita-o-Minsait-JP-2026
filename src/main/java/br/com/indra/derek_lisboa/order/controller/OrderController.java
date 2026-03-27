package br.com.indra.derek_lisboa.order.controller;

import br.com.indra.derek_lisboa.order.dto.OrderDTO;
import br.com.indra.derek_lisboa.order.enums.OrderStatus;
import br.com.indra.derek_lisboa.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Criar pedido (checkout)")
    public ResponseEntity<OrderDTO> createOrder(@Parameter(description = "Email do usuario") @RequestParam String email) {

        OrderDTO order = orderService.createOrder(email);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<OrderDTO> findById(
            @Parameter(description = "ID do pedido") @PathVariable UUID id) {

        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/user")
    @Operation(summary = "Listar pedidos do usuario")
    public ResponseEntity<List<OrderDTO>> findByUser(
            @Parameter(description = "Email do usuario") @RequestParam String email) {

        return ResponseEntity.ok(orderService.findByUser(email));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<OrderDTO> cancelOrder(
            @Parameter(description = "ID do pedido") @PathVariable UUID id) {

        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido")
    public ResponseEntity<OrderDTO> updateStatus(
            @Parameter(description = "ID do pedido") @PathVariable UUID id,
            @Parameter(description = "Novo status do pedido") @RequestParam(name = "status") OrderStatus status) {

        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

}
