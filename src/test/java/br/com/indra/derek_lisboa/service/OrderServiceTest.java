package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.enums.CartStatus;
import br.com.indra.derek_lisboa.model.Cart;
import br.com.indra.derek_lisboa.model.CartItem;
import br.com.indra.derek_lisboa.repository.CartRepository;
import br.com.indra.derek_lisboa.exception.InvalidCartException;
import br.com.indra.derek_lisboa.exception.InvalidOrderStatusException;
import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.dto.OrderDTO;
import br.com.indra.derek_lisboa.enums.OrderStatus;
import br.com.indra.derek_lisboa.model.Order;
import br.com.indra.derek_lisboa.model.OrderItem;
import br.com.indra.derek_lisboa.repository.OrderRepository;
import br.com.indra.derek_lisboa.model.Product;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import br.com.indra.derek_lisboa.model.User;
import br.com.indra.derek_lisboa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryTransactionService inventoryTransactionService;


    @Test
    void shouldCancelOrderAndReturnStock() {

        UUID orderId = UUID.randomUUID();

        Product product = new Product();
        product.setStock(10);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);

        User user = new User();
        user.setEmail("derek@teste.com");

        Order order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setItems(List.of(item));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCELLED, result.status());
        assertEquals(12, product.getStock());

        verify(productRepository).save(product);
        verify(inventoryTransactionService).registerEntry(product, 2);
    }

    @Test
    void shouldThrowExceptionWhenCancelInvalidStatus() {

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class, () -> {
            orderService.cancelOrder(orderId);
        });
    }

    @Test
    void shouldUpdateStatusFromCreatedToPaid() {

        UUID orderId = UUID.randomUUID();

        User user = new User();
        user.setEmail("derek@teste.com");

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        OrderDTO result = orderService.updateStatus(orderId, OrderStatus.PAID);

        assertEquals(OrderStatus.PAID, result.status());
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class, () -> {
            orderService.updateStatus(orderId, OrderStatus.DELIVERED);
        });
    }

    @Test
    void shouldCreateOrderSuccessfully() {

        String email = "derek@teste.com";

        User user = new User();
        user.setEmail(email);

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Memoria");
        product.setBrand("hyperX");

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setPriceSnapshot(BigDecimal.valueOf(50));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderDTO result = orderService.createOrder(email);

        assertEquals(OrderStatus.CREATED, result.status());
        assertEquals(BigDecimal.valueOf(100), result.total());
        assertEquals(1, result.items().size());
        assertTrue(cart.getItems().isEmpty());
        assertEquals(CartStatus.CHECKED_OUT, cart.getStatus());

        verify(orderRepository).save(any(Order.class));
        verify(cartRepository).save(cart);
    }

    @Test
    void shouldThrowExceptionWhenCartIsEmpty() {

        String email = "derek@teste.com";

        User user = new User();
        user.setEmail(email);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setItems(new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE))
                .thenReturn(Optional.of(cart));

        assertThrows(InvalidCartException.class, () -> {
            orderService.createOrder(email);
        });
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        String email = "derek@teste.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(InvalidUserException.class, () -> {
            orderService.createOrder(email);
        });
    }

    @Test
    void shouldThrowExceptionWhenCartNotFound() {

        String email = "derek@teste.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCartException.class, () -> {
            orderService.createOrder(email);
        });
    }
}