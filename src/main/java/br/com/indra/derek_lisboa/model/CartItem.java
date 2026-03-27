package br.com.indra.derek_lisboa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Product product;

    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal priceSnapshot;

    @ManyToOne
    private Cart cart;

}
