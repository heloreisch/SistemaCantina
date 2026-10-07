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
	private static final String URL = "jdbc:mysql://localhost:3306/sistemacantina";
	private static final String USER = "aluno_cd";
	private static final String SENHA = "aluno_pw";

	// Conexao NOVA a cada chamada. Uma conexao guardada em atributo e
	// derrubada pelo servidor por inatividade, e a aplicacao para de
	// funcionar ate ser reiniciada.
	private Connection abrir() throws SQLException {
		return DriverManager.getConnection(URL, USER, SENHA);
	}

	public void selecionar(Produto a) {
		// 1. as regras do animal sozinho, ja escritas no Model
		String problema = a.validar();
		if (problema != null) {
			throw new IllegalArgumentException(problema);
		}
		// 2. a regra que depende dos OUTROS registros
		if (existe(a.getNome(), a.getCategoria())) {
			throw new IllegalArgumentException("Essa categoria ja tem um produto com esse nome.");
		}
		String sql = "INSERT INTO Produto (nome, categoria, preco) VALUES(?, ?, ?)";
		try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
			// Um set por campo, na ORDEM das colunas do INSERT.
			// Indices comecam em 1. O tipo do set combina com o tipo do
			// atributo: setString para texto, setDouble para decimal.
			ps.setString(1, a.getNome());
			ps.setString(2, a.getCategoria().name().trim());
			ps.setDouble(3, a.getPreco());

			ps.executeUpdate();
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao gravar: " + erro.getMessage(), erro);
		}
	}

	public boolean existe(String nome, Categoria categoria) {
		String sql = "SELECT id FROM produto WHERE nome = ? AND categoria = ?";
		try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nome.trim());
			ps.setString(2, categoria.name().trim());
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next(); // achou pelo menos uma linha?
			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao consultar: " + erro.getMessage(), erro);
		}
	}

	public List<Produto> listarTodos() {
		String sql = "SELECT nome, categoria, preco FROM produto ORDER BY nome";
		List<Produto> lista = new ArrayList<>();
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				// Um get por coluna, alimentando um set do objeto.
				// getString para VARCHAR, getDouble para DECIMAL.
				Produto a = new Produto();

				a.setNome(rs.getString("nome"));
				a.setCategoria(Categoria.valueOf(rs.getString("categoria")));
				a.setPreco(rs.getDouble("preco"));

				lista.add(a);
			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao listar: " + erro.getMessage(), erro);
		}
		return lista;
	}

	public int contar() {
		String sql = "SELECT COUNT(*) FROM produto";
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1); // primeira coluna do resultado
			}
			return 0;
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao contar: " + erro.getMessage(), erro);
		}
	}

	/** Teste de ambiente: rode ANTES de depurar qualquer botao. */
	 public static void main(String[] args) {
	 ProdutoRepositorioBD bd = new ProdutoRepositorioBD();
	 try (Connection con = bd.abrir()) {
	 System.out.println("Conexao OK com " + con.getCatalog());
	 System.out.println("Registros: " + bd.contar());
	 } catch (SQLException e) {
	 System.out.println("Falha: " + e.getMessage());
	 }
	 }
	}



