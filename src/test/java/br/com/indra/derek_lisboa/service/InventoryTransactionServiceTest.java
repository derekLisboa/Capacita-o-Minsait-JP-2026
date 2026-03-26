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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryTransactionServiceTest {

    @InjectMocks
    private InventoryTransactionService service;

    @Mock
    private InventoryTransactionRepository repository;

    @Test
    void shouldReturnAllTransactions(){

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Memoria RAM 32Gb");

        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        inventoryTransaction.setId(UUID.randomUUID());
        inventoryTransaction.setProduct(product);
        inventoryTransaction.setQuantity(5);
        inventoryTransaction.setType(TransactionType.ENTRY);
        inventoryTransaction.setCreatedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(inventoryTransaction));;

        List<InventoryTransactionDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Memoria RAM 32Gb", result.get(0).getProductName());
    }

    @Test
    void shouldReturnTransactionsByProduct(){

        UUID id = UUID.randomUUID();

        Product product1 = new Product();
        product1.setId(id);
        product1.setName("SSD 1Tb");

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Memoria RAM 32Gb");

        InventoryTransaction it1 = new InventoryTransaction();
        it1.setProduct(product1);
        it1.setQuantity(5);
        it1.setType(TransactionType.ENTRY);
        it1.setCreatedAt(LocalDateTime.now());

        InventoryTransaction it2 = new InventoryTransaction();
        it2.setProduct(product2);
        it2.setQuantity(3);
        it2.setType(TransactionType.ENTRY);
        it2.setCreatedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(it1, it2));

        List<InventoryTransactionDTO> result = service.findByProduct(id);

        assertEquals(1, result.size());
        assertEquals("SSD 1Tb", result.get(0).getProductName());

    }

    @Test
    void shouldRegisterEntryTransaction(){

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("SSD 1Tb");

        ArgumentCaptor<InventoryTransaction> captor = ArgumentCaptor.forClass(InventoryTransaction.class);

        service.registerEntry(product, 5);


        verify(repository).save(captor.capture());

        InventoryTransaction saved = captor.getValue();

        assertEquals(product, saved.getProduct());
        assertEquals(5, saved.getQuantity());
        assertEquals(TransactionType.ENTRY, saved.getType());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void shouldRegisterExitTransaction(){

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("SSD 1Tb");

        ArgumentCaptor<InventoryTransaction> captor = ArgumentCaptor.forClass(InventoryTransaction.class);

        service.registerExit(product, 5);

        verify(repository).save(captor.capture());

        InventoryTransaction saved = captor.getValue();

        assertEquals(product, saved.getProduct());
        assertEquals(5, saved.getQuantity());
        assertEquals(TransactionType.EXIT, saved.getType());
        assertNotNull(saved.getCreatedAt());
    }

}