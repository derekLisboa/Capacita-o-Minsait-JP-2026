package br.com.indra.derek_lisboa.order.repository;

import br.com.indra.derek_lisboa.order.model.Order;
import br.com.indra.derek_lisboa.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByUser(User user);

}
