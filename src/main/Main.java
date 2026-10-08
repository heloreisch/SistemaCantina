package main;
import controller.ProdutoController;
import model.ProdutoRepositorioBD;
import view.JanelaProduto;

/**
 * PASSO 9 - junta as tres camadas. Execute SEMPRE esta classe.
 */
public class Main {
 public static void main(String[] args) {
 ProdutoRepositorioBD repositorio = new ProdutoRepositorioBD();
 JanelaProduto view = new JanelaProduto();
 ProdutoController controller = new ProdutoController(repositorio, view);
 controller.iniciarTela();
 }
}