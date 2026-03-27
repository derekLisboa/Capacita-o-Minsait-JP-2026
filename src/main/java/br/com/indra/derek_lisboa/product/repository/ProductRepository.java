package br.com.indra.derek_lisboa.product.repository;

import br.com.indra.derek_lisboa.category.model.Category;
import br.com.indra.derek_lisboa.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByBarCode(String barCode);

    Optional<Product> findByBarCode(String barCode);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategory_NameContainingIgnoreCase(String categoryName);

    List<Product> findByNameContainingIgnoreCaseAndCategory_NameContainingIgnoreCase(
            String name,
            String categoryName
    );

    boolean existsByCategory(Category category);
}
