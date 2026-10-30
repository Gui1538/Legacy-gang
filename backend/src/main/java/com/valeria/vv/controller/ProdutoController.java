package com.valeria.vv.controller;

import com.valeria.vv.dto.ProdutoRequest;
import com.valeria.vv.dto.ProdutoResponse;
import com.valeria.vv.model.Usuario;
import com.valeria.vv.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CAMADA DE APLICAÇÃO - CRUD de produtos.
 * As regras (inclusive "só o dono altera") ficam no ProdutoService.
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /** Todos os produtos do e-commerce; ?busca=texto filtra por nome/descrição. */
    @GetMapping
    public List<ProdutoResponse> listar(@RequestParam(required = false) String busca) {
        return produtoService.listar(busca).stream().map(ProdutoResponse::de).toList();
    }

    /** Somente os produtos cadastrados pelo usuário logado. */
    @GetMapping("/meus")
    public List<ProdutoResponse> listarMeus(@AuthenticationPrincipal Usuario logado,
                                            @RequestParam(required = false) String busca) {
        return produtoService.listarDoUsuario(logado.getId(), busca).stream().map(ProdutoResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return ProdutoResponse.de(produtoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@AuthenticationPrincipal Usuario logado,
                                                 @Valid @RequestBody ProdutoRequest req) {
        var criado = produtoService.criar(logado.getId(), req.paraEntidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponse.de(criado));
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id,
                                     @AuthenticationPrincipal Usuario logado,
                                     @Valid @RequestBody ProdutoRequest req) {
        return ProdutoResponse.de(produtoService.atualizar(id, logado.getId(), req.paraEntidade()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        produtoService.excluir(id, logado.getId());
        return ResponseEntity.noContent().build();
    }
}
