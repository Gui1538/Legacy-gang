import java.util.List;

public class ProdutoController {

    private ProdutoService service = new ProdutoService();

    public List<Produto> listar() {
        return service.listarProdutos();
    }

    public Produto salvar(Produto produto) {
        return service.cadastrarProduto(produto);
    }
}