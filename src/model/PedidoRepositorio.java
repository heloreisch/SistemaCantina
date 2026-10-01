package model;
import java.util.List;
import java.util.ArrayList;
/*
public class PedidoRepositorio {
	
	private List<Pedido> pedidos = new ArrayList<>();
	 public void salvar(Pedido ped) {

		// 1. as regras do objeto sozinho, ja escritas no Model
		 String problema = ped.validar();
		 if (problema != null) {
		 throw new IllegalArgumentException(problema);
		 }
		 // 2. a regra que depende dos OUTROS registros
	//	 if (existe(a.getNome(), a.getDono())) {
	//	 throw new IllegalArgumentException(
	//	 "Este dono ja tem um animal com esse nome.");
	//	 }
		 // 3. so agora grava: valide antes de alterar o estado
		 pedidos.add(ped);
		 }
	 // ver como fazer puxar id autoencmeny p ve
		 public boolean existe(String nome, String dono) {
		 for (Pedido a : pedidos) {
		 // equals e nao ==: a pergunta e sobre CONTEUDO
		 if (a.getNome().equals(nome) && a.getDono().equals(dono)) {
		 return true;
		 }
		 }
		 return false;
		 }
		 public List<Animal> listarTodos() {
		 // devolve uma COPIA: ninguem altera a colecao interna por fora
		 return new ArrayList<>(animais);
		 }
		 public int contar() {
		 return animais.size();
		 }
		}

}
*/