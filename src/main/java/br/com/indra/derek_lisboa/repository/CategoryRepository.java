package br.com.indra.derek_lisboa.repository;

import br.com.indra.derek_lisboa.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    
}