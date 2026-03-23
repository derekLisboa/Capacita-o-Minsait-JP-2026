package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.model.HistoricoPreco;
import br.com.indra.derek_lisboa.repository.HistoricoPrecoRepository;
import br.com.indra.derek_lisboa.service.dto.HistoricoProdutoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoPrecoRepository historicoPrecoRepository;

    public List<HistoricoProdutoDTO> getHistoricoByProdutoId(Long produtoId) {
        return historicoPrecoRepository.findByProdutosId(produtoId)
                .stream()
                .map(historico -> HistoricoProdutoDTO.builder()
                        .id(UUID.fromString(historico.getId()))
                        .produto(historico.getProdutos().getNome())
                        .precoAntigo(historico.getPrecoAntigo())
                        .precoNovo(historico.getPrecoNovo())
                        .dataRegistro(historico.getDataAlteracao())
                        .build()
                )
                .toList();
    }
}