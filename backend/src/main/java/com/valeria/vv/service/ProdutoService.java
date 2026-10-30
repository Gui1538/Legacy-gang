package com.valeria.vv.service;

import com.valeria.vv.exception.AcessoNegadoException;
import com.valeria.vv.exception.RecursoNaoEncontradoException;
import com.valeria.vv.model.Produto;
import com.valeria.vv.model.Usuario;
import com.valeria.vv.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CAMADA DE DOMÍNIO - CRUD de produtos e a regra de negócio principal:
 * um usuário só altera (edita ou exclui) produtos que ele mesmo cadastrou,
 * ou seja, que constam na sua página "Meus produtos".
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final UsuarioService usuarioService;

    public ProdutoService(ProdutoRepository produtoRepository, UsuarioService usuarioService) {
        this.produtoRepository = produtoRepository;
        this.usuarioService = usuarioService;
    }

    /** Todos os produtos do e-commerce (com pesquisa opcional). */
    public List<Produto> listar(String busca) {
        return produtoRepository.buscar(textoDeBusca(busca));
    }

    /** Apenas os produtos cadastrados pelo usuário (com pesquisa opcional). */
    public List<Produto> listarDoUsuario(Long usuarioId, String busca) {
        return produtoRepository.buscarDoUsuario(usuarioId, textoDeBusca(busca));
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
    }

    @Transactional
    public Produto criar(Long usuarioId, Produto dados) {
        Usuario dono = usuarioService.buscarPorId(usuarioId);
        dados.setDono(dono);
        return produtoRepository.save(dados);
    }

    @Transactional
    public Produto atualizar(Long id, Long usuarioId, Produto dados) {
        Produto produto = buscarProdutoDoUsuario(id, usuarioId);
        produto.setNome(dados.getNome());
        produto.setDescricao(dados.getDescricao());
        produto.setPreco(dados.getPreco());
        produto.setImagemUrl(dados.getImagemUrl());
        return produtoRepository.save(produto);
    }

    @Transactional
    public void excluir(Long id, Long usuarioId) {
        Produto produto = buscarProdutoDoUsuario(id, usuarioId);
        produtoRepository.delete(produto);
    }

    /** REGRA DE NEGÓCIO: só o dono do produto pode alterá-lo. */
    private Produto buscarProdutoDoUsuario(Long id, Long usuarioId) {
        Produto produto = buscarPorId(id);
        if (!produto.getDono().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Você só pode alterar produtos cadastrados por você");
        }
        return produto;
    }

    private String textoDeBusca(String busca) {
        return busca == null ? "" : busca.trim();
    }
}
