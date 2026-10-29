
package controller;

import java.util.List;
import model.Produto;
import service.ProdutoService;

public class ProdutoController {

    private ProdutoService service = new ProdutoService();

    public List<Produto> listar() {
        return service.listarProdutos();
    }

    public Produto salvar(Produto produto) {
        return service.cadastrarProduto(produto);
    }
}
