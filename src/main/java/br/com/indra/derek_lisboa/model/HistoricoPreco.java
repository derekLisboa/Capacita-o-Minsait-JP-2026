package br.com.indra.derek_lisboa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historico_preco")
public class HistoricoPreco {

    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produtos_id")
    private Produtos produtos;

    @Column(name = "preco_antigo")
    private BigDecimal precoAntigo;

    @Column(name = "preco_novo")
    private BigDecimal precoNovo;

    @Column(name = "data_alteracao", updatable = false)
    @CreationTimestamp
    private LocalDateTime dataAlteracao;
}
;