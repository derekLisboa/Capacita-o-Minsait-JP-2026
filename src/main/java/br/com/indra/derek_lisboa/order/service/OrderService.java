package br.com.indra.derek_lisboa.order.service;

import br.com.indra.derek_lisboa.cart.enums.CartStatus;
import br.com.indra.derek_lisboa.cart.model.Cart;
import br.com.indra.derek_lisboa.cart.repository.CartRepository;
import br.com.indra.derek_lisboa.exception.InvalidCartException;
import br.com.indra.derek_lisboa.exception.InvalidOrderStatusException;
import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.exception.OrderNotFoundException;
import br.com.indra.derek_lisboa.inventory.service.InventoryTransactionService;
import br.com.indra.derek_lisboa.order.dto.OrderDTO;
import br.com.indra.derek_lisboa.order.dto.OrderItemDTO;
import br.com.indra.derek_lisboa.order.enums.OrderStatus;
import br.com.indra.derek_lisboa.order.model.Order;
import br.com.indra.derek_lisboa.order.model.OrderItem;
import br.com.indra.derek_lisboa.order.repository.OrderRepository;
import br.com.indra.derek_lisboa.product.model.Product;
import br.com.indra.derek_lisboa.product.repository.ProductRepository;
import br.com.indra.derek_lisboa.user.model.User;
import br.com.indra.derek_lisboa.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryTransactionService inventoryTransactionService;

    @Transactional
    public OrderDTO createOrder(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("Usuario nao encontrado"));

        Cart cart = cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseThrow(() -> new InvalidCartException("Carrinho vazio ou inexistente"));

        if (cart.getItems().isEmpty()) {
            throw new InvalidCartException("O carrinho esta vazio");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(cartItem.getProduct());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPriceSnapshot(cartItem.getPriceSnapshot());
                    return item;
                })
                .toList();

        order.setItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPriceSnapshot()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);

        Order saved = orderRepository.save(order);

        cart.setStatus(CartStatus.CHECKED_OUT);
        cart.getItems().forEach(item -> item.setCart(null));
        cart.getItems().clear();

        cartRepository.save(cart);

        return toDTO(saved);
    }


    public OrderDTO findById(UUID id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido nao encontrado"));

        return toDTO(order);
    }


    public List<OrderDTO> findByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("Usuario nao encontrado"));

        return orderRepository.findByUser(user)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public OrderDTO cancelOrder(UUID id){

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido nao encontrado"));

        if (order.getStatus() != OrderStatus.CREATED && order.getStatus() != OrderStatus.PAID) {
            throw new InvalidOrderStatusException(
                    "Pedido so pode ser cancelado se estiver CREATED ou PAID");
        }
        for (OrderItem item : order.getItems()) {

            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
            inventoryTransactionService.registerEntry(product, item.getQuantity());

        }
        order.setStatus(OrderStatus.CANCELLED);

        return toDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderDTO updateStatus(UUID id, OrderStatus newStatus){

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido nao encontrado"));

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == OrderStatus.CANCELLED || currentStatus == OrderStatus.DELIVERED) {
            throw new InvalidOrderStatusException("Pedido nao pode ser alterado");
        }

        switch (currentStatus) {

            case CREATED -> {
                if (newStatus != OrderStatus.PAID && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusException("CREATED so pode ir para PAID ou CANCELLED");
                }
            }
            case PAID -> {
                if (newStatus != OrderStatus.SHIPPED && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusException("PAID so pode ir para SHIPPED ou CANCELLED");
                }
            }
            case SHIPPED -> {
                if (newStatus != OrderStatus.DELIVERED) {
                    throw new InvalidOrderStatusException("SHIPPED so pode ir para DELIVERED");
                }
            }
        }

        if (newStatus == OrderStatus.CANCELLED) {

            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());

                productRepository.save(product);
                inventoryTransactionService.registerEntry(product, item.getQuantity());
            }
        }

        order.setStatus(newStatus);

        return toDTO(orderRepository.save(order));
    }

    private OrderDTO toDTO(Order order){

        List<OrderItemDTO> items = order.getItems().stream()
                .map(this::toItemDTO)
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getUser().getEmail(),
                items,
                order.getTotal(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    private OrderItemDTO toItemDTO(OrderItem item){

        return new OrderItemDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getBrand(),
                item.getPriceSnapshot(),
                item.getQuantity()
        );
    }

}
