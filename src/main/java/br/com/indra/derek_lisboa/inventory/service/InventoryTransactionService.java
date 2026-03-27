package br.com.indra.derek_lisboa.inventory.service;

import br.com.indra.derek_lisboa.inventory.model.InventoryTransaction;
import br.com.indra.derek_lisboa.product.model.Product;
import br.com.indra.derek_lisboa.inventory.enums.TransactionType;
import br.com.indra.derek_lisboa.inventory.repository.InventoryTransactionRepository;
import br.com.indra.derek_lisboa.inventory.dto.InventoryTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryTransactionService {

    private final InventoryTransactionRepository repository;

    public List<InventoryTransactionDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<InventoryTransactionDTO> findByProduct(UUID productId) {
        return repository.findByProduct_IdOrderByCreatedAtDesc(productId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void register(Product product, Integer delta, TransactionType type) {

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setDelta(delta);
        transaction.setType(type);

        repository.save(transaction);
    }

    public void registerEntry(Product product, Integer quantity) {
        register(product, quantity, TransactionType.ENTRY);
    }

    public void registerExit(Product product, Integer quantity) {
        register(product, quantity, TransactionType.EXIT);
    }

    private InventoryTransactionDTO toDTO(InventoryTransaction t) {
        return new InventoryTransactionDTO(
                t.getId(),
                t.getProduct().getId(),
                t.getProduct().getName(),
                t.getDelta(),
                t.getType(),
                t.getCreatedAt()
        );
    }
}