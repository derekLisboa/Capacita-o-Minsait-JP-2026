package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.CategoryNotFoundException;
import br.com.indra.derek_lisboa.exception.InvalidProductNameException;
import br.com.indra.derek_lisboa.exception.InvalidProductPriceException;
import br.com.indra.derek_lisboa.exception.ProductNotFoundException;
import br.com.indra.derek_lisboa.model.Category;
import br.com.indra.derek_lisboa.model.Product;
import br.com.indra.derek_lisboa.repository.CategoryRepository;
import br.com.indra.derek_lisboa.repository.PriceHistoryRepository;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import br.com.indra.derek_lisboa.service.dto.ProductDTO;
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

        ProductDTO dto = new ProductDTO();
        dto.setName("Memoria RAM 32Gb");
        dto.setBrand("HyperX");
        dto.setPrice(BigDecimal.valueOf(5999.99));
        dto.setBarCode("1234567890123");
        dto.setStock(5);
        dto.setCategoryId(categoryId);

        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        savedProduct.setName(dto.getName());
        savedProduct.setBrand(dto.getBrand());
        savedProduct.setPrice(dto.getPrice());
        savedProduct.setBarCode(dto.getBarCode());
        savedProduct.setStock(dto.getStock());
        savedProduct.setCategory(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productsRepository.save(any())).thenReturn(savedProduct);

        ProductDTO result = productService.create(dto);

        assertNotNull(result);
        assertEquals("Memoria RAM 32Gb", result.getName());

        verify(productsRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFound() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Memoria RAM 32Gb");
        dto.setBrand("HyperX");
        dto.setPrice(BigDecimal.valueOf(5999.99));
        dto.setStock(5);
        dto.setCategoryId(UUID.randomUUID());

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            productService.create(dto);
        });
    }

    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {

        ProductDTO dto = new ProductDTO();
        dto.setName("");
        dto.setBrand("HyperX");
        dto.setPrice(BigDecimal.valueOf(5999.99));
        dto.setStock(5);

        assertThrows(InvalidProductNameException.class, () -> {
            productService.create(dto);
        });
    }

    @Test
    void shouldUpdatePriceAndCreateHistory() {

        UUID productId = UUID.randomUUID();
        Category category = new Category();

        Product product = new Product();
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(5999.99));
        product.setCategory(category);

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productsRepository.save(any())).thenReturn(product);

        ProductDTO result = productService.updatePrice(productId, BigDecimal.valueOf(4570.99));

        assertEquals(BigDecimal.valueOf(4570.99), product.getPrice());

        verify(priceHistoryRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenPriceIsInvalid() {
        UUID id = UUID.randomUUID();

        assertThrows(InvalidProductPriceException.class, () -> {
            productService.updatePrice(id, BigDecimal.ZERO);
        });
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundOnUpdatePrice() {
        UUID id = UUID.randomUUID();

        when(productsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updatePrice(id, new BigDecimal("5999.99"));
        });
    }
}