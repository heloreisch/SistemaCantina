
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Pedido;
import model.PedidoRepositorioBD;
import model.Status;
import model.Pagamento;
import view.JanelaPedido;

/**
 * CONTROLLER - PASSO 8 do roteiro.
 *
 * Um metodo por botao, e um actionPerformed que descobre qual foi clicado.
 */

public class PedidoController implements ActionListener {

    private PedidoRepositorioBD repositoriop;
    private JanelaPedido viewp;

    public PedidoController(PedidoRepositorioBD repositoriop,JanelaPedido viewp) {

        this.repositoriop = repositoriop;
        this.viewp = viewp;

     // Um addActionListener por botao. O "this" e o proprio Controller
     // se regi
        this.viewp.getBtnCadastrar().addActionListener(this);
        this.viewp.getBtnLimpar().addActionListener(this);
        this.viewp.getBtnFechar().addActionListener(this);
        this.viewp.getBtnEditar().addActionListener(this);
        this.viewp.getTabela().getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	// == e nao equals: a pergunta e "e exatamente aquele objeto?".
        if (e.getSource() == this.viewp.getBtnCadastrar()) {
            cadastrar();

        } else if (e.getSource() == this.viewp.getBtnLimpar()) {
            this.viewp.limparCampos();

        } else if (e.getSource() == this.viewp.getBtnFechar()) {
            this.viewp.fechar();
        }
    }

    private void cadastrar() {
    	// CONVERSAO: tudo que vem de um JTextField e TEXTO, mesmo que
    	 // pareca numero. Converter e trabalho de quem faz fronteira com a
    	 // tela - por isso este try/catch fica aqui, e nao no Model.
    	 // O replace resolve o detalhe brasileiro: digitamos 12,5 e o Java
    	 // so entende 12

        Pedido pedido = new Pedido();

        // Converter o texto do preço para double
        double precoTotal;

        try {
            precoTotal = Double.parseDouble(
                this.viewp.getTxtPrecoTotal().getText().trim().replace(",", ".")
            );

        } catch (NumberFormatException erro) {
            this.viewp.mostrarErro( "O preço total deve ser um número válido.");
            return;
     }
        pedido.setPrecoTotal(precoTotal);
        pedido.setStatus_pedido(
            (Status) this.viewp.getCbStatus().getSelectedItem()
        );

        pedido.setTipo_pagamento((Pagamento) this.viewp.getCbPagamento().getSelectedItem()
        );

        try {
            this.repositoriop.salvar(pedido);

        } catch (IllegalArgumentException erro) {
            this.viewp.mostrarErro(erro.getMessage());
            return;
        }

        this.viewp.mostrarMensagem( "Pedido cadastrado! Total de pedidos: " + this.repositoriop.contar());
        this.viewp.limparCampos();
    }

    public void iniciarTela() {
        this.viewp.setVisible(true);
    }
    		public void valueChanged(ListSelectionEvent e) {
    			if (e.getValueIsAdjusting() || recarregando || editando) {
    							return;
    			}
    				atualizarDetalhe();
    	}
    			private void atualizarDetalhe() {
    				Pedido ped = this.viewp.getPedidoSelecionado();
    					if (ped == null) {
    							this.viewp.mostrarVazio();
    					} else {
    							this.viewp.mostrarDetalhe(ped);
    					}
    						this.viewp.atualizarBotoes(ped != null, this.editando);
    				}
    						private int idSelecionado() {
    							Pedido ped = this.viewp.getPedidoSelecionado();
    								return (ped == null) ? 0 : ped.getId();
    	}
    						public Pedido getAnimalSelecionado() {
    							int linhaTela = tabela.getSelectedRow();
    							if (linhaTela < 0) {
    							return null;
    							}
    							int linhaModelo = tabela.convertRowIndexToModel(linhaTela);
    							return modelo.getPedido(linhaModelo);
    							}
    						
    						this.esperaBusca = new javax.swing.Timer(300, e -> atualizarTela(idSelecionado()));
    						this.esperaBusca.setRepeats(false);
    						this.view.getTxtBusca().getDocument().addDocumentListener(new DocumentListener() {
    						public void insertUpdate(DocumentEvent e) { buscaMudou(); }
    						public void removeUpdate(DocumentEvent e) { buscaMudou(); }
    						public void changedUpdate(DocumentEvent e) { buscaMudou(); }
    						});
    						atualizarTela(0);
    						}
    						private void buscaMudou() {
    						if (!recarregando) {
    						esperaBusca.restart();
    						}
    						}
    						public List<Animal> listarFiltrado(String especie, String trecho) {
    							boolean todas = (especie == null || especie.isEmpty() || especie.equals("(todas)"));
    							String sql = "SELECT id, nome, especie, peso, dono FROM animal WHERE nome LIKE ? "
    							+ (todas ? "" : "AND especie = ? ") // trecho FIXO, nao vem do usuario
    							+ "ORDER BY nome";
    							List<Animal> lista = new ArrayList<>();
    							try (Connection con = abrir();
    							PreparedStatement ps = con.prepareStatement(sql)) {
    							ps.setString(1, "%" + trecho + "%");
    							if (!todas) {
    							ps.setString(2, especie);
    							}
    							try (ResultSet rs = ps.executeQuery()) {
    							while (rs.next()) {
    							lista.add(new Animal(rs.getInt("id"), rs.getString("nome"),
    							rs.getString("especie"), rs.getDouble("peso"), rs.getString("dono")));
    							}
    							}
    							} catch (SQLException ex) {
    							throw new RuntimeException(ex);
    							}
    							return lista;
    							/** O UNICO metodo que repovoa a tela. Todo evento que muda dados termina aqui. */
    							private void atualizarTela(int idParaSelecionar) {
    							this.recarregando = true;
    							try {
    							String especie = this.view.getEspecieSelecionada();
    							this.view.carregarEspecies(this.bd.listarEspecies(), especie);
    							List<Animal> lista = this.bd.listarFiltrado(
    							this.view.getEspecieSelecionada(), this.view.getBusca());
    							this.view.carregarTabela(lista);
    							this.view.carregarResumo(this.bd.contarPorEspecie());
    							this.view.selecionarPorId(idParaSelecionar);
    							this.view.mostrarStatus(lista.size() + " registro(s) em exibicao.");
    							} finally {
    							this.recarregando = false; // finally: libera mesmo se der excecao
    							}
    							atualizarDetalhe();
    							}
    							
    							/** Preenche o combo e devolve o usuario para a opcao que ele tinha escolhido. */
    							public void carregarEspecies(List<String> especies, String manter) {
    							cbEspecie.removeAllItems();
    							cbEspecie.addItem("(todas)");
    							for (String e : especies) {
    							cbEspecie.addItem(e);
    							}
    							if (manter != null) {
    							cbEspecie.setSelectedItem(manter);
    							}
    							}
    							/** Depois de recarregar, a selecao some. Esta rotina a devolve, procurando pelo id. */
    							public void selecionarPorId(int id) {
    							for (int i = 0; i < modelo.getRowCount(); i++) {
    							if (modelo.getAnimal(i).getId() == id) {
    							int linhaTela = tabela.convertRowIndexToView(i);
    							tabela.setRowSelectionInterval(linhaTela, linhaTela);
    							tabela.scrollRectToVisible(tabela.getCellRect(linhaTela, 0, true));
    							return;
    							}
    							}
    							tabela.clearSelection();
    							}

}
