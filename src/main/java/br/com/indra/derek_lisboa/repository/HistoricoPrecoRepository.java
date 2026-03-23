package br.com.indra.derek_lisboa.repository;

import br.com.indra.derek_lisboa.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, Long> {
    /**
     *Ele cria uma query fazendo busca por produto id
     * Select * from historico_perco where produtos_id = :prodtudoId:
     * param produtoId
     * @return HistoricoPreco
     */
    Set<HistoricoPreco> findByProdutosId(Long produtoId);
}
