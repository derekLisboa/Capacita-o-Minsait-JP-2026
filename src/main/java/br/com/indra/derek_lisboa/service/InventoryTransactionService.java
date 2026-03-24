package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.model.InventoryTransaction;
import br.com.indra.derek_lisboa.repository.InventoryTransactionRepository;
import br.com.indra.derek_lisboa.service.dto.InventoryTransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return repository.findAll()
                .stream()
                .filter(t -> t.getProduct().getId().equals(productId))
                .map(this::toDTO)
                .toList();
    }

    private InventoryTransactionDTO toDTO(InventoryTransaction t) {
        return InventoryTransactionDTO.builder()
                .id(t.getId())
                .productId(t.getProduct().getId())
                .productName(t.getProduct().getName())
                .quantity(t.getQuantity())
                .type(t.getType().name())
                .createdAt(t.getCreatedAt())
                .build();
    }
}