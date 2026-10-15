package main;
import controller.ProdutoController;
import model.ProdutoRepositorioBD;
import view.JanelaProduto;
//pedido
import controller.PedidoController;
import model.PedidoRepositorioBD;
import view.JanelaPedido;

/**
 * PASSO 9 - junta as tres camadas. Execute SEMPRE esta classe.
 */

	public class Main {
		public static void main(String[] args) {
	 
		 ProdutoRepositorioBD repositorio = new ProdutoRepositorioBD();
		 JanelaProduto view = new JanelaProduto();
		 ProdutoController controller = new ProdutoController(repositorio, view);
		controller.iniciarTela();
		 
		 //pedido
		 PedidoRepositorioBD repositoriop = new PedidoRepositorioBD();
		 JanelaPedido viewp = new JanelaPedido();
		 PedidoController controllerp = new PedidoController(repositoriop, viewp);
		 controllerp.iniciarTela();
		 }
		}

