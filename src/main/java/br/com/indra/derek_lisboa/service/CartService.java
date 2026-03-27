package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.enums.CartStatus;
import br.com.indra.derek_lisboa.model.Cart;
import br.com.indra.derek_lisboa.model.CartItem;
import br.com.indra.derek_lisboa.exception.InsufficientStockException;
import br.com.indra.derek_lisboa.exception.InvalidQuantityException;
import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.exception.ProductNotFoundException;
import br.com.indra.derek_lisboa.model.Product;
import br.com.indra.derek_lisboa.repository.CartRepository;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import br.com.indra.derek_lisboa.repository.UserRepository;
import br.com.indra.derek_lisboa.dto.CartDTO;
import br.com.indra.derek_lisboa.dto.CartItemDTO;
import br.com.indra.derek_lisboa.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryTransactionService inventoryTransactionService;

    @Transactional
    public Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("Usuario nao encontrado"));

        return cartRepository.findByUserAndStatus(user, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setStatus(CartStatus.ACTIVE);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public CartDTO addProduct(String email, UUID productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade deve ser maior que zero");
        }

        Cart cart = getOrCreateCart(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        int quantityAlreadyInCart = 0;

        if(existingItem.isPresent()) {
            quantityAlreadyInCart = existingItem.get().getQuantity();
        }

        int totalRequested = quantityAlreadyInCart + quantity;

        if (totalRequested > product.getStock()) {
            throw new InsufficientStockException(
                    "Estoque insuficiente. Disponível: " + product.getStock()
            );
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(totalRequested);
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPriceSnapshot(product.getPrice());
            item.setCart(cart);

            cart.getItems().add(item);
        }

        inventoryTransactionService.registerExit(product, quantity);

        cart = cartRepository.save(cart);

        return toDTO(cart);
    }

    public CartDTO toDTO(Cart cart) {

        List<CartItemDTO> items = cart.getItems().stream()
                .map(this::toItemDTO)
                .toList();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemDTO item : items) {
            BigDecimal subtotal = item.price().multiply(BigDecimal.valueOf(item.quantity()));
            total = total.add(subtotal);
        }

        return new CartDTO(
                cart.getId(),
                cart.getUser().getEmail(),
                items,
                total
        );
    }

    private CartItemDTO toItemDTO(CartItem item) {
        return new CartItemDTO(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getBrand(),
                item.getPriceSnapshot(),
                item.getQuantity()
        );
    }

    @Transactional
    public CartDTO removeProduct(String email, UUID productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade deve ser maior que zero");
        }

        Cart cart = getOrCreateCart(email);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Produto nao esta no carrinho"));

        if (quantity > item.getQuantity()) {
            throw new InvalidQuantityException(
                    "Quantidade para remover maior do que a que tem no carrinho"
            );
        }

        Product product = item.getProduct();

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);

        if (quantity.equals(item.getQuantity())) {
            item.setCart(null);
            cart.getItems().remove(item);
        } else {
            item.setQuantity(item.getQuantity() - quantity);
        }

        inventoryTransactionService.registerEntry(product, quantity);

        cart = cartRepository.save(cart);
        return toDTO(cart);
    }
}