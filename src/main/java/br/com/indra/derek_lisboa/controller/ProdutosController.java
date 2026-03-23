package br.com.indra.derek_lisboa.controller;

import br.com.indra.derek_lisboa.model.Produtos;
import br.com.indra.derek_lisboa.service.ProdutosServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutosServices produtosServices;

    /**
     *
     * Recomendaçao de desenvolvimento, ampliar responses(ResponseEntity)
     * possiveis alem do ok
     */

    @PostMapping("/cria")
    public ResponseEntity<Produtos> criarProduto(Produtos produto){
        return ResponseEntity.ok(produtosServices.createdProduto(produto));
    }

    @GetMapping
    public ResponseEntity<List<Produtos>> getAll(){
        return ResponseEntity.ok(produtosServices.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produtos> getById(@PathVariable Long id, @RequestParam String nome){
        return ResponseEntity.ok(produtosServices.getById(id));
    }

    @PutMapping("/atualiza/{id}")
    public ResponseEntity<Produtos> atualizarProduto(@RequestParam Long id, @RequestBody Produtos produto){
        return ResponseEntity.ok(produtosServices.atualiza(produto));
    }

    @PatchMapping("/atualiza-parcial")
    public ResponseEntity<Produtos> atualizarProdutoParcial(@RequestParam Long id, @RequestParam BigDecimal preco){
        return ResponseEntity.ok(produtosServices.atualizaPreco(id, preco));
    }

    @DeleteMapping("/deleta/{id}")
    public ResponseEntity<Produtos> deletarProduto(Long id){
        produtosServices.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
