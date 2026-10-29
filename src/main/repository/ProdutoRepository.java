
package repository;

import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class ProdutoRepository {

    private List<Produto> produtos = new ArrayList<>();

    public List<Produto> obterTodos() {
        return produtos;
    }

    public Produto salvar(Produto produto) {
        produtos.add(produto);
        return produto;
    }
}
