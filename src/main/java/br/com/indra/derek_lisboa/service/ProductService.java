package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.model.Category;
import br.com.indra.derek_lisboa.model.PriceHistory;
import br.com.indra.derek_lisboa.model.Product;
import br.com.indra.derek_lisboa.repository.CategoryRepository;
import br.com.indra.derek_lisboa.repository.PriceHistoryRepository;
import br.com.indra.derek_lisboa.repository.ProductRepository;
import br.com.indra.derek_lisboa.service.dto.ProductDTO;
import br.com.indra.derek_lisboa.service.dto.ProductHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public Product create(ProductDTO dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setBarCode(dto.getBarCode());
        product.setCategory(category);

        return productsRepository.save(product);
    }

    public List<Product> getAll() {
        return productsRepository.findAll();
    }

    public Product getById(UUID id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Product update(UUID id, ProductDTO dto) {

        Product product = getById(id);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setBarCode(dto.getBarCode());
        product.setCategory(category);

        return productsRepository.save(product);
    }

    public void delete(UUID id) {
        Product product = getById(id);
        productsRepository.delete(product);
    }

    public Product updatePrice(UUID id, BigDecimal price) {

        Product product = getById(id);

        BigDecimal oldPrice = product.getPrice();

        product.setPrice(price);

        PriceHistory history = new PriceHistory();
        history.setProduct(product);
        history.setOldPrice(oldPrice);
        history.setNewPrice(price);

        priceHistoryRepository.save(history);

        return productsRepository.save(product);
    }

    public List<ProductHistoryDTO> getPriceHistory(UUID productId) {

        List<PriceHistory> history = priceHistoryRepository
                .findByProduct_Id(productId);

        return history.stream()
                .map(h -> ProductHistoryDTO.builder()
                        .id(h.getId())
                        .product(h.getProduct().getName()) // ou productName se você mudou
                        .oldPrice(h.getOldPrice())
                        .newPrice(h.getNewPrice())
                        .registerDate(h.getAlterationDate())
                        .build()
                )
                .toList();
    }
}