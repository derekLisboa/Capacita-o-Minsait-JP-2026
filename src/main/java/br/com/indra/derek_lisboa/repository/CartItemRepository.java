package br.com.indra.derek_lisboa.repository;

import br.com.indra.derek_lisboa.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {


}
