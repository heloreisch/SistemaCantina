package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MODEL - guarda os pedidos no MySQL e aplica a regra do conjunto.
 * Nao ha nenhum "javax.swing" aqui: da para testar sem abrir tela.
 */
public class PedidoRepositorioBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sistemacantina";
    private static final String USER = "aluno_cd";
    private static final String SENHA = "aluno_pw";

    // Conexao NOVA a cada chamada (uma conexao guardada e derrubada por inatividade).
    private Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, USER, SENHA);
    }

    // true se o pedido existe e esta com status = finalizado 
    private boolean estaFinalizado(int idPedido) {
        String sql = "SELECT status_pedido FROM pedido WHERE id_pedido = ?";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
        	//substitui o primeiro ? pelo identificador do pedido
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && "FINALIZADO".equals(rs.getString("status_pedido"));
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao verificar pedido: " + erro.getMessage(), erro);
        }
    }

    //  devolve o id gerado para o proprio objeto
    public void salvar(Pedido pedido) {
        String problema = pedido.validar();
        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }
        	
        //add pedido
        String sql = "INSERT INTO pedido (produtos, preco_total, status_pedido, tipo_pagamento) VALUES (?, ?, ?, ?)";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, resumo(pedido.getProdutos()));
            ps.setDouble(2, pedido.getPrecoTotal());
            ps.setString(3, pedido.getStatus_pedido().name());
            ps.setString(4, pedido.getTipo_pagamento().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setIdPedido(rs.getInt(1)); // devolve o id novo ao objeto
                }
            }
            gravarItens(con, pedido.getIdPedido(), pedido.getProdutos());
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao salvar pedido: " + erro.getMessage(), erro);
        }
    }

    //  retriçao = pedido finalizado nao pode ser alterado. //
    public void alterar(Pedido pedido) {
        String problema = pedido.validar();
        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }

        String sql = "UPDATE pedido SET produtos = ?, preco_total = ?, status_pedido = ?, tipo_pagamento = ? "
                   + "WHERE id_pedido = ? AND status_pedido <> 'FINALIZADO'";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, resumo(pedido.getProdutos()));
            ps.setDouble(2, pedido.getPrecoTotal());
            ps.setString(3, pedido.getStatus_pedido().name());
            ps.setString(4, pedido.getTipo_pagamento().name());
            ps.setInt(5, pedido.getIdPedido());

            int linhasAlteradas = ps.executeUpdate();
            if (linhasAlteradas == 0) {
                if (estaFinalizado(pedido.getIdPedido())) {
                    throw new IllegalArgumentException("Não é possível alterar um pedido finalizado.");
                }
                throw new IllegalArgumentException("Pedido não encontrado.");
            }
            gravarItens(con, pedido.getIdPedido(), pedido.getProdutos());
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao alterar pedido: " + erro.getMessage(), erro);
        }
    }

    //  deletar itens primeiro, depois o pedido, finalizado nao exclui //
    public void excluir(int idPedido) {
        if (estaFinalizado(idPedido)) {
            throw new IllegalArgumentException("Não é possível excluir um pedido finalizado.");
        }
        String sqlItens = "DELETE FROM item_pedido WHERE id_pedido = ?";
        String sqlPedido = "DELETE FROM pedido WHERE id_pedido = ? AND status_pedido <> 'FINALIZADO'";

        try (Connection con = abrir()) {
            con.setAutoCommit(false);
            try (PreparedStatement p1 = con.prepareStatement(sqlItens);
                 PreparedStatement p2 = con.prepareStatement(sqlPedido)) {
                p1.setInt(1, idPedido);
                p1.executeUpdate();
                p2.setInt(1, idPedido);
                int n = p2.executeUpdate();
                if (n == 0) {
                    con.rollback();
                    throw new IllegalArgumentException("Pedido não encontrado.");
                }
                con.commit();
            } catch (SQLException | RuntimeException erro) {
                con.rollback();
                throw erro;
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao excluir pedido: " + erro.getMessage(), erro);
        }
    }

    // coxinha, suco -> texto da coluna produtos pra ser usado na busca 
    private String resumo(List<Produto> produtos) {
        StringBuilder sb = new StringBuilder();
        for (Produto p : produtos) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(p.getNome());
        }
        return sb.toString();
    }

    // regrava os itens do pedido em item_pedido apaga os antigos e insere os atuais 
    private void gravarItens(Connection con, int idPedido, List<Produto> produtos) throws SQLException {
        String sqlDel = "DELETE FROM item_pedido WHERE id_pedido = ?";
        String sqlIns = "INSERT IGNORE INTO item_pedido (id_pedido, id_produto) "
                      + "SELECT ?, id FROM produto WHERE nome = ? LIMIT 1";
        try (PreparedStatement del = con.prepareStatement(sqlDel);
             PreparedStatement ins = con.prepareStatement(sqlIns)) {
            del.setInt(1, idPedido);
            del.executeUpdate();
            for (Produto p : produtos) {
                ins.setInt(1, idPedido);
                ins.setString(2, p.getNome());
                ins.executeUpdate();
            }
        }
    }

    
     // converte o texto da categoria no enum Categoria ignora maiusculas e tal e acentos para nao quebrar quando o banco tem algado e o enum e SALGADO.
     
    private Categoria lerCategoria(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("Produto sem categoria no banco.");
        }
        String chave = Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace(' ', '_');
        for (Categoria c : Categoria.values()) {
            if (c.name().equalsIgnoreCase(chave)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Categoria desconhecida no banco: '" + texto
                + "'. Valores aceitos: " + Arrays.toString(Categoria.values()));
    }

    // monta o produto a partir de uma linha com nome, preco e categoria
    private Produto montarProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto(null, 0, null);
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setCategoria(lerCategoria(rs.getString("categoria")));
        return produto;
    }

    /** Produtos cadastrados na tela de produtos (alimenta o combo do pedido). */
    public List<Produto> listarProdutos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT nome, preco, categoria FROM produto ORDER BY nome";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(montarProduto(rs));
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar produtos: " + erro.getMessage(), erro);
        }
        return lista;
    }

    // lista os produtos do pedido
    private ArrayList<Produto> buscarProdutos(int idPedido) {
        ArrayList<Produto> produtos = new ArrayList<>();
        String sql = "SELECT p.nome, p.preco, p.categoria FROM produto p "
                   + "INNER JOIN item_pedido ip ON p.id = ip.id_produto WHERE ip.id_pedido = ?";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    produtos.add(montarProduto(rs));
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao buscar produtos do pedido " + idPedido
                    + ": " + erro.getMessage(), erro);
        }
        return produtos;
    }

    private Pedido montar(ResultSet rs) throws SQLException {
        Pedido ped = new Pedido();
        ped.setIdPedido(rs.getInt("id_pedido"));
        ped.setProdutos(buscarProdutos(ped.getIdPedido()));
        ped.setPrecoTotal(rs.getDouble("preco_total"));
        ped.setStatus_pedido(Status.valueOf(rs.getString("status_pedido")));
        ped.setTipo_pagamento(Pagamento.valueOf(rs.getString("tipo_pagamento")));
        return ped;
    }

    public List<Pedido> listarTodos() {
        return listarFiltrado(null, "");
    }

    /**
     * Filtra por status (null = todos) e por um trecho que pode ser parte do
     * id ou do texto dos produtos. O trecho SQL concatenado e FIXO; o que vem
     * do usuario entra sempre por "?".
     */
    public List<Pedido> listarFiltrado(Status status, String trecho) {
        String sql = "SELECT id_pedido, preco_total, status_pedido, tipo_pagamento FROM pedido "
                   + "WHERE (CAST(id_pedido AS CHAR) LIKE ? OR produtos LIKE ?) "
                   + (status == null ? "" : "AND status_pedido = ? ")
                   + "ORDER BY id_pedido";
        List<Pedido> lista = new ArrayList<>();
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + trecho + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            if (status != null) {
                ps.setString(3, status.name());
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar pedidos: " + erro.getMessage(), erro);
        }
        return lista;
    }

    /** Quantos pedidos existem em cada status (alimenta os cartoes de resumo). */
    public Map<String, Integer> contarPorStatus() {
        Map<String, Integer> totais = new LinkedHashMap<>();
        String sql = "SELECT status_pedido, COUNT(*) AS total FROM pedido GROUP BY status_pedido ORDER BY status_pedido";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                totais.put(rs.getString("status_pedido"), rs.getInt("total"));
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao contar pedidos por status: " + erro.getMessage(), erro);
        }
        return totais;
    }

    public int contar() {
        String sql = "SELECT COUNT(*) FROM pedido";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao contar pedidos: " + erro.getMessage(), erro);
        }
        return 0;
    }

    /** Teste de ambiente: rode ANTES de depurar qualquer botao. */
    public static void main(String[] args) {
        PedidoRepositorioBD bd = new PedidoRepositorioBD();
        try (Connection con = bd.abrir()) {
            System.out.println("Conexao OK com " + con.getCatalog());
            System.out.println("Registros: " + bd.contar());
        } catch (SQLException e) {
            System.out.println("Falha: " + e.getMessage());
        }
    }
}