package br.com.indra.derek_lisboa.repository;

import br.com.indra.derek_lisboa.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {

    List<PriceHistory> findByProduct_Id(UUID productId);

}