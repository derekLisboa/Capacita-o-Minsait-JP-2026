package br.com.indra.derek_lisboa.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricoProdutoDTO {

    private UUID id;
    private String produto;
    private BigDecimal precoAntigo;
    private BigDecimal precoNovo;
    private LocalDateTime dataRegistro;
}
