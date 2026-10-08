package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepositorioBD {
	
	
	private static final String URL = "jdbc:mysql://localhost:3306/sistemacantina";
	private static final String USER = "aluno_cd";
	private static final String SENHA = "aluno_pw";
	
	// Conexao NOVA a cada chamada. Uma conexao guardada em atributo e
	 // derrubada pelo servidor por inatividade, e a aplicacao para de
	 // funcionar ate ser reiniciada.
	 private Connection abrir() throws SQLException {
	 return DriverManager.getConnection(URL, USER, SENHA);
	 
	 
	 public void salvar(Pedido pedido) {
		 // 1. as regras do animal sozinho, ja escritas no Model
		 String problema = a.validar();
		 if (problema != null) {
		 throw new IllegalArgumentException(problema);
		 }
		 // 2. a regra que depende dos OUTROS registros
		 if (existe(a.getNome(), a.getDono())) {
		 throw new IllegalArgumentException(
		 "Este dono ja tem um animal com esse nome.");
		 }
		 String sql = "INSERT INTO pedido () VALUES
		(?, ?, ?, ?)";
		 try (Connection con = abrir();
	 }
	
}
