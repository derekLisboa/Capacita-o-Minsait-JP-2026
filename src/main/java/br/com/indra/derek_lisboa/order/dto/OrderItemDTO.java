package br.com.indra.derek_lisboa.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDTO(

        UUID productId,
        String productName,
        String brand,
        BigDecimal price,
        Integer quantity

) {}
