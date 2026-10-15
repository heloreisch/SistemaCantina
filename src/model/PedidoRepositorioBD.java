
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
 * PASSO 6 do roteiro. Repare que ele tem os MESMOS metodos que a versao
 * em memoria do Passo 5: salvar, listarTodos, contar. So mudou o destino.
 *
 * Procure "javax.swing" neste arquivo: nao ha nenhum. E por isso que da
 * para testar esta classe sem abrir tela.
 */

public class PedidoRepositorioBD {

    private static final String URL = "jdbc:mysql://localhost:3306/sistemacantina";
    private static final String USER = "aluno_cd";
    private static final String SENHA = "aluno_pw";

 // Conexao NOVA a cada chamada. Uma conexao guardada em atributo e
    // derrubada pelo servidor por inatividade, e a aplicacao para de
    // funcionar ate ser reiniciada.

    private Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, USER, SENHA);
    }

    private boolean existe(int idPedido) {
    		
    	// select o status do pedido da tabela pedido onde o id do pedido for..
        String sql = "SELECT status_pedido FROM pedido WHERE id_pedido = ?";

        try (Connection con = abrir(); //abre a conexao com o banco de dados
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido); //troca o  ? da consulta pelo valor do id

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && "FINALIZADO".equals( rs.getString("status_pedido"));
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                    "Erro ao verificar pedido.", erro);
        }
    }

    // 1. as regras do pedido sozinho, ja escritas no Model
    public void salvar(Pedido pedido) {

        String problema = pedido.validar();

        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }

        String sql = "INSERT INTO pedido (produtos, preco_total, status_pedido, tipo_pagamento) VALUES (?, ?, ?, ?)";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {

        	// Um set por campo, na ORDEM das colunas do INSERT.
        	 // Indices comecam em 1. O tipo do set combina com o tipo do
        	 // atributo: setString para texto, setDouble para decimal.
        	
            ps.setString(1, pedido.getProdutos().toString());
            ps.setDouble(2, pedido.getPrecoTotal());
            ps.setString(3, pedido.getStatus_pedido().name());
            ps.setString(4, pedido.getTipo_pagamento().name());
            ps.executeUpdate();

        } catch (SQLException erro) {
            throw new RuntimeException(
                    "Erro ao salvar pedido.", erro);
        }
    }

    public void alterar(Pedido pedido) {

        String problema = pedido.validar();

        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }

        String sql = "UPDATE pedido SET produtos = ?, preco_total = ?, status_pedido = ?, tipo_pagamento = ? WHERE id_pedido = ? AND status_pedido <> 'FINALIZADO'";
        try (Connection con = abrir();																										//só permite atualizar se o status atual for diferente d finalizado
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getProdutos().toString());
            ps.setDouble(2, pedido.getPrecoTotal());
            ps.setString(3, pedido.getStatus_pedido().name());
            ps.setString(4, pedido.getTipo_pagamento().name());
            ps.setInt(5, pedido.getIdPedido());

            int linhasAlteradas = ps.executeUpdate(); // vai recebe a quantidade de registros que foram alterados
            if (linhasAlteradas == 0) { 
                if (existe(pedido.getIdPedido())) {
                    throw new IllegalArgumentException("Não é possível alterar um pedido finalizado.");
                }

                throw new IllegalArgumentException(
                        "Pedido não encontrado.");
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                    "Erro ao alterar pedido.", erro);
        }
    }

    	// lista os produtos do pedido
    private List<Produto> buscarProdutos(int idPedido) {
        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT p.nome, p.preco, p.categoria FROM produto p INNER JOIN item_pedido ip ON p.id_produto = ip.id_produto WHERE ip.id_pedido = ?";
        try (Connection con = abrir();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { // achou pelo menos uma linha?
                    Produto produto = new Produto();

                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setCategoria(Categoria.valueOf(rs.getString("categoria")));
                    
                    produtos.add(produto);

                }
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                    "Erro ao buscar produtos do pedido.", erro);
        }

        return produtos;
    }

    
    public List<Pedido> listarTodos() {

        String sql = "SELECT id_pedido, preco_total, status_pedido, tipo_pagamento FROM pedido ORDER BY id_pedido";
        	List<Pedido> lista = new ArrayList<>();
		        try (Connection con = abrir();
		             PreparedStatement ps = con.prepareStatement(sql);
		             ResultSet rs = ps.executeQuery()) {
		        		while (rs.next()) {

		                Pedido ped = new Pedido();
		                
		                ped.setIdPedido(rs.getInt("id_pedido"));
		                ped.setProdutos((ArrayList<Produto>) buscarProdutos(ped.getIdPedido()));
		                ped.setPrecoTotal( rs.getDouble("preco_total"));
		                ped.setStatus_pedido( Status.valueOf(rs.getString("status_pedido")));
		                ped.setTipo_pagamento(  Pagamento.valueOf( rs.getString("tipo_pagamento")));

		                lista.add(ped);
            }

        } catch (SQLException erro) {
            throw new RuntimeException(
                    "Erro ao listar pedidos.", erro);
        }

        return lista;
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
            throw new RuntimeException(
                    "Erro ao contar pedidos.", erro);
        }

        return 0;
    }
    
    
    public void actionPerformed(ActionEvent e) {
    	if (recarregando) {
    	return; // o combo dispara evento enquanto e preenchido
    	}
    	Object origem = e.getSource();
    	if (origem == this.viewp.getBtnNovo()) {
    	this.view.limparSelecao();
    	entrarEdicao(null);
    	} else if (origem == this.viewp.getBtnEditar()) {
    	entrarEdicao(this.viewp.getAnimalSelecionado());
    	} else if (origem == this.view.getBtnExcluir()) {
    	excluir();
    	} else if (origem == this.view.getBtnSalvar()) {
    	salvar();
    	} else if (origem == this.view.getBtnCancelar()) {
    	this.editando = false;
    	atualizarDetalhe();
    	} else if (origem == this.view.getCbEspecie()) {
    	atualizarTela(idSelecionado());
    	}
    	}
    	private void entrarEdicao(Animal a) {
    	this.editando = true;
    	this.view.editarAnimal(a);
    	this.view.atualizarBotoes(a != null, true);
    	}
    	private void salvar() {
    	try {
    	Animal a = this.view.lerFormulario();
    	if (a.getNome().isEmpty()) {
    	this.view.mostrarStatus("Informe o nome.");
    	return;
    	}
    	this.bd.salvar(a);
    	this.editando = false;
    	atualizarTela(a.getId());
    	this.view.mostrarStatus("Salvo: " + a.getNome());
    	} catch (NumberFormatException ex) {
    	this.view.mostrarStatus("Peso invalido. Use numeros, ex.: 4.5");
    	}
    	}
    	
    	private void excluir() {
    	Animal a = this.view.getAnimalSelecionado();
    	if (a != null && this.view.confirmar("Excluir " + a.getNome() + "?")) {
    	this.bd.excluir(a.getId());
    	atualizarTela(0);
    	public void salvar(Animal a) throws SQLException {
    		boolean novo = (a.getId() == 0);
    		String sql = novo
    		? "INSERT INTO animal (nome, especie, peso, dono) VALUES (?, ?, ?, ?)"
    		: "UPDATE animal SET nome = ?, especie = ?, peso = ?, dono = ? WHERE id = ?";
    		try (Connection con = abrir();
    		PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    		ps.setString(1, ped.getNome());
    		ps.setString(2, ped.getEspecie());
    		ps.setDouble(3, ped.getPeso());
    		ps.setString(4, ped.getDono());
    		if (novo) {
    		ps.executeUpdate();
    		try (ResultSet rs = ps.getGeneratedKeys()) {
    		if (rs.next()) {
    		a.setId(rs.getInt(1)); // devolve o id novo ao objeto
    		}
    		}
    		} else {
    		ps.setInt(5, a.getId());
    		ps.executeUpdate();
    		}
    		}
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

