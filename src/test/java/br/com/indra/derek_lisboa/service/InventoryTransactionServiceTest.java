package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.inventory.model.InventoryTransaction;
import br.com.indra.derek_lisboa.inventory.service.InventoryTransactionService;
import br.com.indra.derek_lisboa.product.model.Product;
import br.com.indra.derek_lisboa.inventory.enums.TransactionType;
import br.com.indra.derek_lisboa.inventory.repository.InventoryTransactionRepository;
import br.com.indra.derek_lisboa.inventory.dto.InventoryTransactionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryTransactionServiceTest {

    @InjectMocks
    private InventoryTransactionService service;

    @Mock
    private InventoryTransactionRepository repository;

    @Test
    void shouldReturnAllTransactions() {

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Memoria RAM 32Gb");

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setId(UUID.randomUUID());
        transaction.setProduct(product);
        transaction.setDelta(5);
        transaction.setType(TransactionType.ENTRY);
        transaction.setCreatedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(transaction));

        List<InventoryTransactionDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Memoria RAM 32Gb", result.get(0).productName());
    }

    @Test
    void shouldReturnTransactionsByProduct() {

        UUID id = UUID.randomUUID();

        Product product = new Product();
        product.setId(id);
        product.setName("SSD 1Tb");

        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setProduct(product);
        transaction.setDelta(5);
        transaction.setType(TransactionType.ENTRY);
        transaction.setCreatedAt(LocalDateTime.now());

        when(repository.findByProduct_IdOrderByCreatedAtDesc(id))
                .thenReturn(List.of(transaction));

        List<InventoryTransactionDTO> result = service.findByProduct(id);

        assertEquals(1, result.size());
        assertEquals("SSD 1Tb", result.get(0).productName());
    }

    @Test
    void shouldRegisterEntryTransaction() {

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("SSD 1Tb");

        ArgumentCaptor<InventoryTransaction> captor =
                ArgumentCaptor.forClass(InventoryTransaction.class);

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.registerEntry(product, 5);

        verify(repository).save(captor.capture());

        InventoryTransaction saved = captor.getValue();

        assertEquals(product, saved.getProduct());
        assertEquals(5, saved.getDelta());
        assertEquals(TransactionType.ENTRY, saved.getType());
    }

    @Test
    void shouldRegisterExitTransaction() {

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("SSD 1Tb");

        ArgumentCaptor<InventoryTransaction> captor =
                ArgumentCaptor.forClass(InventoryTransaction.class);

        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.registerExit(product, 5);

        verify(repository).save(captor.capture());

        InventoryTransaction saved = captor.getValue();

        assertEquals(product, saved.getProduct());
        assertEquals(5, saved.getDelta());
        assertEquals(TransactionType.EXIT, saved.getType());
    }
}