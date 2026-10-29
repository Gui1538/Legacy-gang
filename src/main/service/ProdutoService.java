
package service;

import java.util.List;
import model.Produto;
import repository.ProdutoRepository;

public class ProdutoService {

    private ProdutoRepository repository = new ProdutoRepository();

    public List<Produto> listarProdutos() {
        return repository.obterTodos();
    }

    public Produto cadastrarProduto(Produto produto) {
        return repository.salvar(produto);
    }
}
