package br.com.indra.derek_lisboa.repository;

import br.com.indra.derek_lisboa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByBarCode(String barCode);

    Optional<Product> findByBarCode(String barCode);

    List<Product> findByName(String name);

    List<Product> findByCategoryName(String categoryName);

    List<Product> findByNameAndCategoryName(String name, String categoryName);
}
