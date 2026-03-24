package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.exception.*;
import br.com.indra.derek_lisboa.model.Category;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public ProductDTO create(ProductDTO dto) {
        validateProductDTO(dto);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoria não encontrada"));

        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setBarCode(dto.getBarCode());
        product.setCategory(category);

        Product saved = productsRepository.save(product);
        return toDTO(saved);
    }

    public List<ProductDTO> getAll() {
        return productsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getById(UUID id) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado"));
        return toDTO(product);
    }

    public ProductDTO update(UUID id, ProductDTO dto) {

        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));

        validateProductDTOForUpdate(product, dto);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoria nao encontrada"));
        product.setCategory(category);

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setBarCode(dto.getBarCode());

        Product updated = productsRepository.save(product);
        return toDTO(updated);
    }

    private void validateProductDTOForUpdate(Product product, ProductDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidProductNameException("O Nome do produto é obrigatório");
        }
        if (dto.getBrand() == null || dto.getBrand().isBlank()) {
            throw new InvalidProductBrandException("A Marca do produto é obrigatória");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException("O Preço deve ser maior que zero");
        }

        if (dto.getBarCode() != null) {
            Optional<Product> existing = productsRepository.findByBarCode(dto.getBarCode());
            if (existing.isPresent() && !existing.get().getId().equals(product.getId())) {
                throw new InvalidProductBarCodeException("Já existe um produto com esse código de barras");
            }
        }
    }

    public void delete(UUID id) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));
        productsRepository.delete(product);
    }

    public ProductDTO updatePrice(UUID id, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException("O Preço deve ser maior que zero");
        }

        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto nao encontrado"));

        BigDecimal oldPrice = product.getPrice();
        product.setPrice(price);

        var history = new br.com.indra.derek_lisboa.model.PriceHistory();
        history.setProduct(product);
        history.setOldPrice(oldPrice);
        history.setNewPrice(price);
        priceHistoryRepository.save(history);

        Product updated = productsRepository.save(product);
        return toDTO(updated);
    }

    public List<ProductHistoryDTO> getPriceHistory(UUID productId) {
        return priceHistoryRepository.findByProduct_Id(productId)
                .stream()
                .map(h -> ProductHistoryDTO.builder()
                        .id(h.getId())
                        .product(h.getProduct().getName())
                        .oldPrice(h.getOldPrice())
                        .newPrice(h.getNewPrice())
                        .registerDate(h.getAlterationDate())
                        .build())
                .collect(Collectors.toList());
    }

    private ProductDTO toDTO(Product product){
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setBarCode(product.getBarCode());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }

    public List<ProductDTO> searchByName(String name) {
        return productsRepository.findByName(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchByCategory(String category) {
        return productsRepository.findByCategoryName(category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchByNameAndCategory(String name, String category) {
        return productsRepository.findByNameAndCategoryName(name, category)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private void validateProductDTO(ProductDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InvalidProductNameException("O Nome do produto é obrigatório");
        }
        if (dto.getBrand() == null || dto.getBrand().isBlank()) {
            throw new InvalidProductBrandException("A Marca do produto é obrigatória");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductPriceException("O Preço deve ser maior que zero");
        }
        if (dto.getBarCode() != null && productsRepository.existsByBarCode(dto.getBarCode())) {
            throw new InvalidProductBarCodeException("Já existe um produto com esse código de barras");
        }
    }
}