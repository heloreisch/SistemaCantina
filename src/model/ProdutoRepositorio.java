package model;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositorio {

	private List<Produto> produtos = new ArrayList<>();

	public void selecionar(Produto a) {
		// 1. as regras do objeto sozinho, ja escritas no Model
		String problema = a.validar();
		if (problema != null) {
			throw new IllegalArgumentException(problema);
		}

		// 2. a regra que depende dos OUTROS registros - NAO EXISTE EM PRODUTOS
		if (existe(a.getNome(), a.getCategoria())) {
			throw new IllegalArgumentException("Essa categoria ja contém esse produto");
		}

		produtos.add(a);
	}

	public boolean existe(String nome, Categoria categoria) {
		for (Produto a : produtos) {
			// equals e nao ==: a pergunta e sobre CONTEUDO
			if (a.getNome().equals(nome) && a.getCategoria().equals(categoria)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Produto> listarTodos() {
		 // devolve uma COPIA: ninguem altera a colecao interna por fora
		 return new ArrayList<>(produtos);
		 }
		 public int contar() {
		 return produtos.size();
		 }

}
