package br.com.indra.derek_lisboa.service;

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
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

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
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toDTO(product);
    }

    public ProductDTO update(UUID id, ProductDTO dto) {
        validateProductDTO(dto);

        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setBarCode(dto.getBarCode());
        product.setCategory(category);

        Product updated = productsRepository.save(product);
        return toDTO(updated);
    }

    public void delete(UUID id) {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        productsRepository.delete(product);
    }

    public ProductDTO updatePrice(UUID id, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço deve ser maior que zero");
        }

        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

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
    //Conversão para DTO
    private ProductDTO toDTO(Product product){
        ProductDTO dto = new ProductDTO();
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
            throw new RuntimeException("Nome do produto é obrigatório");
        }
        if (dto.getBrand() == null || dto.getBrand().isBlank()) {
            throw new RuntimeException("Marca do produto é obrigatória");
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Preço deve ser maior que zero");
        }
    }
}