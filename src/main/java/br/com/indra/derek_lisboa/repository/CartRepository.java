package br.com.indra.derek_lisboa.repository;

import br.com.indra.derek_lisboa.enums.CartStatus;
import br.com.indra.derek_lisboa.model.Cart;
import br.com.indra.derek_lisboa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUserAndStatus(User user, CartStatus status);
}
