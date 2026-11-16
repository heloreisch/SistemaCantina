package model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	public void salvar(Produto a) {
	    boolean novo = (a.getId() == 0);
	    String sql = novo
	        ? "INSERT INTO produto (nome, categoria, preco) VALUES (?, ?, ?)"
	        : "UPDATE produto SET nome = ?, categoria = ?, preco = ? WHERE id = ?";
	    try (Connection con = abrir();
	         PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setString(1, a.getNome());
	        ps.setString(2, a.getCategoria().name());
	        ps.setDouble(3, a.getPreco());
	        if (novo) {
	            ps.executeUpdate();
	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) a.setId(rs.getInt(1));
	            }
	        } else {
	            ps.setInt(4, a.getId());
	            ps.executeUpdate();
	        }
	    } catch (SQLException erro) {
	        throw new RuntimeException("Erro ao salvar produto: " + erro.getMessage(), erro);
	    }
	}

	public void excluir(int idProduto) {
	    String sql = "DELETE FROM produto WHERE id = ?";
	    try (Connection con = abrir(); PreparedStatement stmt = con.prepareStatement(sql)) {

			stmt.setInt(1, idProduto);
			stmt.executeUpdate();

		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao excluir produto: " + erro.getMessage(), erro);
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
		String sql = "SELECT id, nome, categoria, preco, status " + "FROM produto ORDER BY nome";
		List<Produto> lista = new ArrayList<>();
		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				// Um get por coluna, alimentando um set do objeto.
				// getString para VARCHAR, getDouble para DECIMAL.
				Produto a = new Produto();

				a.setId(rs.getInt("id"));
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

	/**
	 * Filtra por status (null = todos) e por um trecho que pode ser parte do id ou
	 * do texto dos produtos. O trecho SQL concatenado e FIXO; o que vem do usuario
	 * entra sempre por "?".
	 */
	public List<Produto> listarFiltrado(String categoria, String trecho) {

		String sql = "SELECT id, nome, categoria, preco FROM produto " + "WHERE nome LIKE ? ";

		boolean filtrarCategoria = categoria != null && !categoria.equals("(todas)") && !categoria.isEmpty();

		if (filtrarCategoria) {
			sql += "AND categoria = ? ";
		}

		sql += "ORDER BY nome";

		List<Produto> lista = new ArrayList<>();

		try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, "%" + trecho + "%");

			if (filtrarCategoria) {
				ps.setString(2, categoria);
			}

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					Produto p = new Produto();

					p.setId(rs.getInt("id"));
					p.setNome(rs.getString("nome"));
					p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
					p.setPreco(rs.getDouble("preco"));

					lista.add(p);
				}
			}

		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao listar produtos: " + erro.getMessage(), erro);
		}

		return lista;
	}

	public List<Categoria> listarCategorias() {
		String sql = "SELECT COLUMN_TYPE FROM information_schema.COLUMNS " + "WHERE TABLE_SCHEMA = DATABASE() "
				+ "AND LOWER(TABLE_NAME) = 'produto' AND COLUMN_NAME = 'categoria'";
		List<Categoria> lista = new ArrayList<>();

		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				String tipo = rs.getString(1);
				String miolo = tipo.substring(tipo.indexOf('(') + 1, tipo.lastIndexOf(')'));
				for (String v : miolo.split(",")) {
					lista.add(Categoria.valueOf(v.replace("'", "").trim()));
				}

			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao listar categorias: " + erro.getMessage(), erro);
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

	public Map<String, Integer> contarPorCategoria() {

		String sql = "SELECT categoria, COUNT(*) AS total " + "FROM produto GROUP BY categoria";

		Map<String, Integer> resumo = new LinkedHashMap<>();

		try (Connection con = abrir();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String categoria = rs.getString("categoria");
				int total = rs.getInt("total");

				resumo.put(categoria, total);
			}

		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao contar produtos por categoria: " + erro.getMessage(), erro);
		}

		return resumo;
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

	public List<Produto> listarPorCategoria(Categoria categoria) {
		String sql = "SELECT id, nome, categoria, preco FROM produto WHERE categoria = ? ORDER BY nome";
		List<Produto> lista = new ArrayList<>();
		try (Connection con = abrir(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, categoria.name());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Produto p = new Produto();
					p.setId(rs.getInt("id"));
					p.setNome(rs.getString("nome"));
					p.setCategoria(Categoria.valueOf(rs.getString("categoria")));
					p.setPreco(rs.getDouble("preco"));
					lista.add(p);
				}
			}
		} catch (SQLException erro) {
			throw new RuntimeException("Erro ao listar: " + erro.getMessage(), erro);
		}
		return lista;
	}

}
