import controller.ProdutoController;
import model.Produto;

public class Main {
    public static void main(String[] args) {
        // Criar o controlador para testar as camadas
        ProdutoController controller = new ProdutoController();

        // 1. Criar um produto de teste
        Produto p1 = new Produto();
        p1.setId(1L);
        p1.setNome("Caderno Inteligente");
        p1.setPreco(25.90);
        controller.salvar(p1);

        Produto p2 = new Produto();
        p2.setId(2L);
        p2.setNome("Caneta Azul");
        p2.setPreco(3.50);
        controller.salvar(p2);

        // 2. Listar os produtos e mostrar no terminal
        System.out.println("=== TESTE DA POC A FUNCIONAR ===");
        for (Produto prod : controller.listar()) {
            System.out.println("Produto: " + prod.getNome() + " | Preço: R$ " + prod.getPreco());
        }
    }
}