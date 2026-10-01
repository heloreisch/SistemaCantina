package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MODEL - guarda os animais no MySQL e aplica a regra do conjunto.
 *
 * PASSO 6 do roteiro. Repare que ele tem os MESMOS metodos que a versao em
 * memoria do Passo 5: salvar, listarTodos, contar. So mudou o destino.
 *
 * Procure "javax.swing" neste arquivo: nao ha nenhum. E por isso que da para
 * testar esta classe sem abrir tela.
 */

public class ProdutoRepositorioBD {

	// Os tres dados da conexao num lugar so.
	private static final String URL = "jdbc:mysql://localhost:3306/meusistema";
	private static final String USER = "aluno_cd";
	private static final String SENHA = "aluno_pw";

	// Conexao NOVA a cada chamada. Uma conexao guardada em atributo e
	 // derrubada pelo servidor por inatividade, e a aplicacao para de
	 // funcionar ate ser reiniciada.
	 private Connection abrir() throws SQLException {
	 return DriverManager.getConnection(URL, USER, SENHA);
	 }
	 public void salvar(Produto a) {
		 // 1. as regras do animal sozinho, ja escritas no Model
		 String problema = a.validar();
		 if (problema != null) {
		 throw new IllegalArgumentException(problema);
		 }
		 // 2. a regra que depende dos OUTROS registros
		 if (existe(a.getNome(), a.getCategoria())) {
		 throw new IllegalArgumentException(
		 "Essa categoria ja tem um produto com esse nome.");
		 }
		 String sql = "INSERT INTO Produto (nome, categoria, preco) VALUES(?, ?, ?)";
		 try (Connection con = abrir();
				 
	public static void main(String[] args) {
	}

}
