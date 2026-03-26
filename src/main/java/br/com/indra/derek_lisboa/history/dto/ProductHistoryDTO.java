package br.com.indra.derek_lisboa.history.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductHistoryDTO {

    private UUID id;
    private String product;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private LocalDateTime registerDate;
}
