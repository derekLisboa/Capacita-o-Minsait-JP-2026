package br.com.indra.derek_lisboa.product.service;

import br.com.indra.derek_lisboa.exception.*;
import br.com.indra.derek_lisboa.category.model.Category;
import br.com.indra.derek_lisboa.history.model.PriceHistory;
import br.com.indra.derek_lisboa.product.model.Product;
import br.com.indra.derek_lisboa.category.repository.CategoryRepository;
import br.com.indra.derek_lisboa.history.repository.PriceHistoryRepository;
import br.com.indra.derek_lisboa.product.repository.ProductRepository;
import br.com.indra.derek_lisboa.product.dto.ProductDTO;
import br.com.indra.derek_lisboa.history.dto.ProductHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    @Transactional
    public ProductDTO create(ProductDTO dto) {

        validateProductDTO(dto);

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada"));

        Product product = new Product();
        product.setName(dto.name().trim());
        product.setBrand(dto.brand().trim());
        product.setPrice(dto.price());
        product.setBarCode(dto.barCode() != null ? dto.barCode().trim() : null);
        product.setCategory(category);
        product.setStock(dto.stock());

        Product saved = productsRepository.save(product);
        return toDTO(saved);
    }

    public List<ProductDTO> findAll() {
        return productsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ProductDTO getById(UUID id) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        return toDTO(product);
    }

    @Transactional
    public ProductDTO update(UUID id, ProductDTO dto) {

        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));

        validateProductDTOForUpdate(product, dto);

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));
        product.setCategory(category);

        product.setName(dto.name().trim());
        product.setBrand(dto.brand().trim());
        product.setPrice(dto.price());
        product.setBarCode(dto.barCode() != null ? dto.barCode().trim() : null);
        product.setStock(dto.stock());

        Product updated = productsRepository.save(product);
        return toDTO(updated);
    }

    private void validateProductDTOForUpdate(Product product, ProductDTO dto) {
        if (dto.barCode() != null) {
            Optional<Product> existing = productsRepository.findByBarCode(dto.barCode());

            if (existing.isPresent() &&
                    !existing.get().getId().equals(product.getId())) {

                throw new InvalidProductBarCodeException(
                        "Já existe um produto com esse código de barras");
            }
        }
    }

    @Transactional
    public void delete(UUID id) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));
        productsRepository.delete(product);
    }

    @Transactional
    public ProductDTO updatePrice(UUID id, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException("O Preço deve ser maior que zero");
        }

        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));

        BigDecimal oldPrice = product.getPrice();
        product.setPrice(price);

        Product updated = productsRepository.save(product);

        var history = new PriceHistory();
        history.setProduct(updated);
        history.setOldPrice(oldPrice);
        history.setNewPrice(price);

        priceHistoryRepository.save(history);

        return toDTO(updated);
    }

    public List<ProductHistoryDTO> getPriceHistory(UUID productId) {
        return priceHistoryRepository.findByProduct_IdOrderByAlterationDateDesc(productId)
                .stream()
                .map(h -> new ProductHistoryDTO(
                        h.getId(),
                        h.getProduct().getName(),
                        h.getOldPrice(),
                        h.getNewPrice(),
                        h.getAlterationDate()
                ))
                .toList();
    }

    private ProductDTO toDTO(Product product){
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getBarCode(),
                product.getStock(),
                product.getCategory().getId()
        );
    }

    public List<ProductDTO> searchByName(String name) {
        return productsRepository.findByNameContainingIgnoreCase(name.trim())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ProductDTO> searchByCategory(String category) {
        return productsRepository.findByCategory_NameContainingIgnoreCase(category.trim())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ProductDTO> searchByNameAndCategory(String name, String category) {
        return productsRepository.findByNameContainingIgnoreCaseAndCategory_NameContainingIgnoreCase(name.trim(), category.trim())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void validateProductDTO(ProductDTO dto) {
        if (dto.barCode() != null && productsRepository.existsByBarCode(dto.barCode())) {
            throw new InvalidProductBarCodeException("Já existe um produto com esse código de barras");
        }
    }
}