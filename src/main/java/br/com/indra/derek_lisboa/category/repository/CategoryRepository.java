package br.com.indra.derek_lisboa.category.repository;

import br.com.indra.derek_lisboa.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameIgnoreCaseAndParent(String name, Category parent);

    boolean existsByParent(Category parent);

    boolean existsByCategory(Category category);

}