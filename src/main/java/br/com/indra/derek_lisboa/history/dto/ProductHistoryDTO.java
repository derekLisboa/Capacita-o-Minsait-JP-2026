package br.com.indra.derek_lisboa.history.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductHistoryDTO(
        UUID id,
        String productName,
        BigDecimal oldPrice,
        BigDecimal newPrice,
        LocalDateTime alterationDate
) {}
