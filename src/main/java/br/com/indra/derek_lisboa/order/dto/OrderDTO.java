package br.com.indra.derek_lisboa.order.dto;

import br.com.indra.derek_lisboa.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDTO(

        UUID id,
        String userEmail,
        List<OrderItemDTO> items,
        BigDecimal total,
        OrderStatus status,
        LocalDateTime createdAt

) {}
