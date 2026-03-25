package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.InsufficientStockException;
import br.com.indra.derek_lisboa.exception.InvalidQuantityException;
import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.exception.ProductNotFoundException;
import br.com.indra.derek_lisboa.model.*;
import br.com.indra.derek_lisboa.repository.CartRepository;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import br.com.indra.derek_lisboa.repository.UserRepository;
import br.com.indra.derek_lisboa.service.dto.CartDTO;
import br.com.indra.derek_lisboa.service.dto.CartItemDTO;
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

    public Cart getOrCreateCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("Usuario nao encontrado"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    @Transactional
    public Cart addProduct(String email, UUID productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade deve ser maior que zero");
        }

        Cart cart = getOrCreateCart(email);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        int quantityAlreadyInCart = existingItem.map(CartItem::getQuantity).orElse(0);
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
            item.setCart(cart);

            cart.getItems().add(item);
        }

        inventoryTransactionService.registerExit(product, quantity);

        return cartRepository.save(cart);
    }

    public CartDTO toDTO(Cart cart) {

        List<CartItemDTO> items = cart.getItems().stream().map(item ->
                CartItemDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .brand(item.getProduct().getBrand())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .build()
        ).toList();

        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDTO.builder()
                .id(cart.getId())
                .userEmail(cart.getUser().getEmail())
                .items(items)
                .total(total)
                .build();
    }

    @Transactional
    public Cart removeProduct(String email, UUID productId, Integer quantity) {

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
            cart.getItems().remove(item);
        } else {
            item.setQuantity(item.getQuantity() - quantity);
        }

        inventoryTransactionService.registerEntry(product, quantity);

        return cartRepository.save(cart);
    }
}