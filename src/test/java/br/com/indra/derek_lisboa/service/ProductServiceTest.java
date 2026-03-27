package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.*;
import br.com.indra.derek_lisboa.category.model.Category;
import br.com.indra.derek_lisboa.product.model.Product;
import br.com.indra.derek_lisboa.product.service.ProductService;
import br.com.indra.derek_lisboa.category.repository.CategoryRepository;
import br.com.indra.derek_lisboa.history.repository.PriceHistoryRepository;
import br.com.indra.derek_lisboa.product.repository.ProductRepository;
import br.com.indra.derek_lisboa.product.dto.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PriceHistoryRepository priceHistoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfully() {

        UUID categoryId = UUID.randomUUID();

        Category category = new Category();
        category.setId(categoryId);

        ProductDTO dto = new ProductDTO(
                null,
                "Memoria RAM 32Gb",
                "HyperX",
                BigDecimal.valueOf(5999.99),
                "1234567890123",
                5,
                categoryId
        );

        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        savedProduct.setName(dto.name());
        savedProduct.setBrand(dto.brand());
        savedProduct.setPrice(dto.price());
        savedProduct.setBarCode(dto.barCode());
        savedProduct.setStock(dto.stock());
        savedProduct.setCategory(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(savedProduct);

        ProductDTO result = productService.create(dto);

        assertNotNull(result);
        assertEquals("Memoria RAM 32Gb", result.name());

        verify(productsRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        ProductDTO dto = new ProductDTO(
                null,
                "Memoria RAM 32Gb",
                "HyperX",
                BigDecimal.valueOf(5999.99),
                null,
                5,
                UUID.randomUUID()
        );

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> productService.create(dto));
    }

    @Test
    void shouldThrowExceptionWhenBarCodeAlreadyExists() {

        UUID categoryId = UUID.randomUUID();

        ProductDTO dto = new ProductDTO(
                null,
                "Memoria RAM",
                "HyperX",
                BigDecimal.valueOf(5999.99),
                "1234567890123",
                5,
                categoryId
        );

        when(productsRepository.existsByBarCode("1234567890123")).thenReturn(true);

        assertThrows(InvalidProductBarCodeException.class,
                () -> productService.create(dto));
    }

    @Test
    void shouldUpdatePriceAndCreateHistory() {

        UUID productId = UUID.randomUUID();

        Category category = new Category();
        category.setId(UUID.randomUUID());

        Product product = new Product();
        product.setId(productId);
        product.setName("Memoria");
        product.setBrand("HyperX");
        product.setPrice(BigDecimal.valueOf(5999.99));
        product.setCategory(category);

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productsRepository.save(any())).thenReturn(product);

        ProductDTO result = productService.updatePrice(productId, BigDecimal.valueOf(4570.99));

        assertEquals(BigDecimal.valueOf(4570.99), result.price());

        verify(priceHistoryRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsInvalid() {

        UUID id = UUID.randomUUID();

        assertThrows(InvalidProductPriceException.class,
                () -> productService.updatePrice(id, BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdatePrice() {

        UUID id = UUID.randomUUID();

        when(productsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.updatePrice(id, new BigDecimal("5999.99")));
    }
}