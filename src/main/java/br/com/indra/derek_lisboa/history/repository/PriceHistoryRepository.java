package br.com.indra.derek_lisboa.history.repository;

import br.com.indra.derek_lisboa.history.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, UUID> {

    List<PriceHistory> findByProduct_IdOrderByAlterationDateDesc(UUID productId);

}