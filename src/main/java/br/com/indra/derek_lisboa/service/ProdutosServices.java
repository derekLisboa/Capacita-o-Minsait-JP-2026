package br.com.indra.derek_lisboa.service;

import br.com.indra.derek_lisboa.model.HistoricoPreco;
import br.com.indra.derek_lisboa.model.Produtos;
import br.com.indra.derek_lisboa.repository.HistoricoPrecoRepository;
import br.com.indra.derek_lisboa.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutosServices {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoRepository historicoPrecoRepository;

    public Produtos createdProduto(Produtos produto){
        return produtosRepository.save(produto);
    }

    public Produtos atualiza(Produtos produto){
        return produtosRepository.save(produto);
    }

    public void deletarProduto(Long id){
        produtosRepository.deleteById(id);
    }

    public List<Produtos> getAll(){
        return produtosRepository.findAll();
    }

    public Produtos getById(Long id){
        return produtosRepository.findById(id).get();
    }

    public Produtos atualizaPreco(Long id, BigDecimal preco){
    //    Produtos produto = produtosRepository.findById(id).get();
        final var  produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setPreco(preco);
        /***
         * Rastreabilidade
         * 1 - Criar um log
         * 2 - Adicionar em tabela historico de preços valores old e new
         * para cada produto atualizado
         * 3 - Antes de atualizar a tabela de produto, pegar o valor atual na tabela e adicionar
         * na tabela historico
         * 4 - Pegar novo valor da tabela e adicionar na tabela historico
         * 5 - Sempre na tabela adicionar novo resgistro após atualizar tabela de produto
         * Estrutura da tabela historico de preços
         * id
         * id_produto
         * preco_antigo
         * preco_novo
         * data_alteracao
         * */
        final var historico = new HistoricoPreco();
        historico.setPrecoAntigo(produto.getPreco());
        historico.setProdutos(produto);
        historico.setPrecoNovo(preco);
        historicoPrecoRepository.saveAndFlush(historico);

        //Exemplo de nao se fazer por gerar retrabalho
    //    final var historicoNovo = new historicoPrecoRepository.findById(historico.getId().get());
    //    historicoNovo.setPrecoNovo(preco);
    //   historicoPrecoRepository.saveAndFlush(historicoNovo);
        /***
         * get na tabela produto para novo preço
         */

        return produtosRepository.saveAndFlush(produto);
    }
}
