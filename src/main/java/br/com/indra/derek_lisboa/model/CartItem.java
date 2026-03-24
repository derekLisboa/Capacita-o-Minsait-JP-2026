package br.com.indra.derek_lisboa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    private Cart cart;

}
