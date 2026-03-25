package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.InsufficientStockException;
import br.com.indra.derek_lisboa.exception.InvalidQuantityException;
import br.com.indra.derek_lisboa.model.Cart;
import br.com.indra.derek_lisboa.model.CartItem;
import br.com.indra.derek_lisboa.model.Product;
import br.com.indra.derek_lisboa.model.User;
import br.com.indra.derek_lisboa.repository.CartRepository;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import br.com.indra.derek_lisboa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InventoryTransactionService inventoryTransactionService;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddProductToCartSuccessfully() {

        String email = "derek@teste.com";
        UUID productId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Product product = new Product();
        product.setId(productId);
        product.setStock(5);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.save(any())).thenReturn(cart);

        Cart result = cartService.addProduct(email, productId, 2);

        assertNotNull(result);
        assertEquals(3, product.getStock());

        verify(inventoryTransactionService).registerExit(product, 2);

    }

    @Test
    void shouldThrowExceptionWhenStockIsInsufficient() {

        String email = "derek@email.com";
        UUID productId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Product product = new Product();
        product.setId(productId);
        product.setStock(1);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> {
            cartService.addProduct(email, productId, 5);
        });
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsInvalid() {

        assertThrows(InvalidQuantityException.class, () -> {
            cartService.addProduct("email", UUID.randomUUID(), 0);
        });
    }

    @Test
    void shouldRemoveProductFromCartSuccessfully() {

        String email = "derek@teste.com";
        UUID productId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Product product = new Product();
        product.setId(productId);
        product.setStock(5);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        item.setCart(cart);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        Cart result = cartService.removeProduct(email, productId, 2);

        assertNotNull(result);
        assertEquals(7, product.getStock());
        assertTrue(cart.getItems().isEmpty());

        verify(inventoryTransactionService).registerEntry(product, 2);
    }

    @Test
    void shouldDecreaseQuantityWhenRemovingPartially() {

        String email = "derek@teste.com";
        UUID productId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Product product = new Product();
        product.setId(productId);
        product.setStock(5);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(5);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        item.setCart(cart);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        Cart result = cartService.removeProduct(email, productId, 2);

        assertNotNull(result);
        assertEquals(7, product.getStock());
        assertFalse(cart.getItems().isEmpty());
        assertEquals(3, item.getQuantity());

        verify(inventoryTransactionService).registerEntry(product, 2);
    }

    @Test
    void shouldThrowExceptionWhenRemovingMoreThanExists() {

        String email = "derek@teste.com";
        UUID productId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);

        Product product = new Product();
        product.setId(productId);
        product.setStock(5);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);

        item.setCart(cart);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        assertThrows(InvalidQuantityException.class, () -> {
            cartService.removeProduct(email, productId, 5);
        });

        assertEquals(5, product.getStock());

        verify(inventoryTransactionService, never())
                .registerEntry(any(), any());
    }

}