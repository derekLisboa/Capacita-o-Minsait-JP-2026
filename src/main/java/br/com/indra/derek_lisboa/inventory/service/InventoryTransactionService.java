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
        return repository.findByProduct_Id(productId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void registerExit(Product product, Integer quantity) {

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setQuantity(quantity);
        transaction.setType(TransactionType.EXIT);
        transaction.setCreatedAt(LocalDateTime.now());

        repository.save(transaction);
    }

    public void registerEntry(Product product, Integer quantity) {

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setQuantity(quantity);
        transaction.setType(TransactionType.ENTRY);
        transaction.setCreatedAt(LocalDateTime.now());

        repository.save(transaction);
    }

    private InventoryTransactionDTO toDTO(InventoryTransaction t) {
        return new InventoryTransactionDTO(
                t.getId(),
                t.getProduct().getId(),
                t.getProduct().getName(),
                t.getQuantity(),
                t.getType(),
                t.getCreatedAt()
        );
    }
}