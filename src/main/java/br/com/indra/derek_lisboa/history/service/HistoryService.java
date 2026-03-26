package br.com.indra.derek_lisboa.history.service;

import br.com.indra.derek_lisboa.history.repository.PriceHistoryRepository;
import br.com.indra.derek_lisboa.history.dto.ProductHistoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    public List<ProductHistoryDTO> getHistoryByProductId(UUID productId) {
        return priceHistoryRepository.findByProduct_Id(productId)
                .stream()
                .map(history -> ProductHistoryDTO.builder()
                        .id(history.getId())
                        .product(history.getProduct().getName())
                        .oldPrice(history.getOldPrice())
                        .newPrice(history.getNewPrice())
                        .registerDate(history.getAlterationDate())
                        .build()
                )
                .toList();
    }
}