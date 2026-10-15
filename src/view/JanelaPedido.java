package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Pagamento;
import model.Status;

public class JanelaPedido extends JFrame {

    private JPanel contentPane;

    // Campos de texto
    private JTextField txtIdPedido;
    private JTextField txtProdutos;
    private JTextField txtPrecoTotal;

    // combox
    private JComboBox<Status> cbStatus;
    private JComboBox<Pagamento> cbPagamento;

    // btn
    private JButton btnCadastrar;
    private JButton btnLimpar;
    private JButton btnFechar;

    public JanelaPedido() {

        setTitle("Cadastro de Pedidos - Cantina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 330);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // ID do pedido
        JLabel lblIdPedido = new JLabel("ID do pedido");
        lblIdPedido.setBounds(10, 21, 100, 14);
        contentPane.add(lblIdPedido);

        txtIdPedido = new JTextField();
        txtIdPedido.setBounds(120, 18, 110, 20);
        contentPane.add(txtIdPedido);

        // Produtos
        JLabel lblProdutos = new JLabel("Produtos");
        lblProdutos.setBounds(10, 60, 100, 14);
        contentPane.add(lblProdutos);

        txtProdutos = new JTextField();
        txtProdutos.setBounds(120, 57, 110, 20);
        contentPane.add(txtProdutos);

        // Preço total
        JLabel lblPrecoTotal = new JLabel("Preço total");
        lblPrecoTotal.setBounds(10, 99, 100, 14);
        contentPane.add(lblPrecoTotal);

        txtPrecoTotal = new JTextField();
        txtPrecoTotal.setBounds(120, 96, 110, 20);
        contentPane.add(txtPrecoTotal);

        // status
        JLabel lblStatus = new JLabel("Status");
        lblStatus.setBounds(10, 138, 100, 14);
        contentPane.add(lblStatus);

        cbStatus = new JComboBox<>(Status.values());
        cbStatus.setBounds(120, 135, 110, 22);
        contentPane.add(cbStatus);

        //  pagamneto
        JLabel lblPagamento = new JLabel("Pagamento");
        lblPagamento.setBounds(10, 177, 100, 14);
        contentPane.add(lblPagamento);
        
        

        cbPagamento = new JComboBox<>(Pagamento.values());
        cbPagamento.setBounds(120, 174, 110, 22);
        contentPane.add(cbPagamento);

        // btncadastrar
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(270, 17, 105, 22);
        contentPane.add(btnCadastrar);

        // btnlimpar
        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(270, 56, 105, 22);
        contentPane.add(btnLimpar);

        // btnfechar
        btnFechar = new JButton("Fechar");
        btnFechar.setBounds(270, 95, 105, 22);
        contentPane.add(btnFechar);
    }

    // ---- Grupo 1: entregar componentes ao Controller ----

    public JTextField getTxtIdPedido() {
        return txtIdPedido;
    }

    public JTextField getTxtProdutos() {
        return txtProdutos;
    }

    public JTextField getTxtPrecoTotal() {
        return txtPrecoTotal;
    }

    public JComboBox<Status> getCbStatus() {
        return cbStatus;
    }

    public JComboBox<Pagamento> getCbPagamento() {
        return cbPagamento;
    }

    public JButton getBtnCadastrar() {
        return btnCadastrar;
    }

    public JButton getBtnLimpar() {
        return btnLimpar;
    }

    public JButton getBtnFechar() {
        return btnFechar;
    }

    // ---- Grupo 2: capacidades da própria tela ----

    public void mostrarMensagem(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    public void mostrarErro(String texto) {
        JOptionPane.showMessageDialog(
            this,
            texto,
            "Atenção",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public void limparCampos() {
        txtIdPedido.setText("");
        txtProdutos.setText("");
        txtPrecoTotal.setText("");

        cbStatus.setSelectedIndex(0);
        cbPagamento.setSelectedIndex(0);

        txtIdPedido.requestFocus();
    }
    
    
 // O gerenciador precisa ser atributo: e nele que chamamos show()
    // depois, para trocar de cartao.
    private CardLayout cards = new CardLayout();
    private JPanel painelCards = new JPanel(cards);
    private JButton btnIrCadastro = new JButton("Cadastro");
    private JButton btnIrLista = new JButton("Consulta");
    // cartao 1
    private JTextField txtNome = new JTextField(16);
    private JTextField txtEspecie = new JTextField(16);
    private JTextField txtPeso = new JTextField(6);
    private JTextField txtDono = new JTextField(16);
    private JButton btnSalvar = new JButton("Salvar");
    // cartao 2
    private JComboBox<String> cbEspecie = new JComboBox<>();
    private JButton btnFiltrar = new JButton("Filtrar");
    private AnimalTableModel modelo = new AnimalTableModel();
    private JTable tabela = new JTable(modelo);
    private JLabel lblStatus = new JLabel(" Pronto.");
    public JanelaSistema() {
    setTitle("Sistema De Cantina - layout adaptavel");
			    setDefaultCloseOperation(EXIT_ON_CLOSE);
			    setSize(640, 400);
			    setMinimumSize(new Dimension(520, 320)); // impede encolher demais
			    setLayout(new BorderLayout(6, 6));
			    add(criarBarraNavegacao(), BorderLayout.NORTH);
			    painelCards.add(criarCartaoCadastro(), "cadastro");
			    painelCards.add(criarCartaoLista(), "lista");
			    add(painelCards, BorderLayout.CENTER);
			    add(lblStatus, BorderLayout.SOUTH);
			    }
				    private JPanel criarBarraNavegacao() {
				    JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
				    barra.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				    barra.add(btnIrCadastro);
				    barra.add(btnIrLista);
				    return barra;
				    }
				    /** Cartao 1: formulario com GridBagLayout. */
				    private JPanel criarCartaoCadastro() {
						    JPanel p = new JPanel(new GridBagLayout());
						    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
						    GridBagConstraints g = new GridBagConstraints();
						    g.insets = new Insets(5, 6, 5, 6);
						    	g.fill = GridBagConstraints.HORIZONTAL;
						    	String[] rotulos = { "Nome:", "Especie:", "Peso (kg):", "Dono:" };
						    	JTextField[] campos = { txtNome, txtEspecie, txtPeso, txtDono };
						    		for (int i = 0; i < rotulos.length; i++) {
						    			g.gridx = 0; g.gridy = i; g.weightx = 0;
						    			g.anchor = GridBagConstraints.EAST;
						    			p.add(new JLabel(rotulos[i]), g);
						    			g.gridx = 1; g.weightx = 1;
						    			p.add(campos[i], g);
				    }
		    JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
		    	botoes.add(btnSalvar);
		    		g.gridx = 0; g.gridy = rotulos.length; g.gridwidth = 2; g.weightx = 1;
		    			p.add(botoes, g);
						    // Empurra tudo para cima: uma celula vazia que fica com a sobra
						    // vertical. Sem ela, o formulario se espalha pelo painel inteiro.
		    					g.gridy = rotulos.length + 1; g.weighty = 1;
		    					g.fill = GridBagConstraints.BOTH;
		    						p.add(new JPanel(), g);
		    							return p;
							    }
				    	/** Cartao 2: filtro + tabela, com BorderLayout. */
				    		private JPanel criarCartaoLista() {
				    			JPanel p = new JPanel(new BorderLayout(6, 6));
				    				p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
				    					JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
				    							filtro.add(new JLabel("Especie:"));
				    							filtro.add(cbEspecie);
				    							filtro.add(btnFiltrar);
				    							p.add(filtro, BorderLayout.NORTH);
				    								tabela.setRowHeight(22);
				    								tabela.setAutoCreateRowSorter(true); // clicar no cabecalho ordena
				    								p.add(new JScrollPane(tabela), BorderLayout.CENTER);
				    									return p;
    }
    // ---- Grupo 1: componentes para o Controller -----------------------
    public JButton getBtnIrCadastro() { return btnIrCadastro; }
    public JButton getBtnIrLista() { return btnIrLista; }
    public JButton getBtnFiltrar() { return btnFiltrar; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JComboBox<String> getCbEspecie() { return cbEspecie; }
    public JTable getTabela() { return tabela; }
    // ---- Grupo 2: capacidades da tela ---------------------------------
    /** Troca o cartao visivel. O nome tem que ser o mesmo do add(). */
    public void mostrarCartao(String nome) {
    cards.show(painelCards, nome);
    }
    /** Recebe a lista pronta e manda o modelo se redesenhar. */
    public void carregarTabela(java.util.List<model.Animal> lista) {
    modelo.setDados(lista);
    }
    /** Preenche o combo com o que veio do banco. */
    public void carregarEspecies(java.util.List<String> especies) {
    cbEspecie.removeAllItems();
    cbEspecie.addItem("(todas)");
    for (String e : especies) {
    cbEspecie.addItem(e);
    }
    }
    public String getEspecieSelecionada() {
    Object sel = cbEspecie.getSelectedItem();
    return (sel == null) ? "" : sel.toString();
    }
    public void mostrarStatus(String texto) {
    lblStatus.setText(" " + texto);
    }
   }
    
    private JPanel criarPainelDetalhe() {
    	painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
    	painelDetalhe.setPreferredSize(new Dimension(250, 0)); // largura que o EAST pede
    	painelDetalhe.add(criarCartaoVazio(), "vazio");
    	painelDetalhe.add(criarCartaoVer(), "ver");
    	painelDetalhe.add(criarCartaoEditar(), "editar");
    	return painelDetalhe;
    	}
    	/** Estado "nada selecionado": uma mensagem, em vez de campos em branco. */
    	private JPanel criarCartaoVazio() {
    		JLabel msg = new JLabel("<html><center>Selecione um pedido<br>na tabela.</center></html>",
    				SwingConstants.CENTER);
    			msg.setForeground(Color.GRAY);
    				JPanel p = new JPanel(new BorderLayout());
    					p.add(msg, BorderLayout.CENTER);
    							return p;
    	}
    	
    	/** Estado "um animal selecionado": rotulos e valores, so leitura. */
    	private JPanel criarCartaoVer() {
    		JPanel p = new JPanel(new GridBagLayout());
    		GridBagConstraints g = new GridBagConstraints();
    			g.insets = new Insets(5, 6, 5, 6);
    				String[] rotulos = { "id:", "produtos", "preço total", "tipo de pagamnto", "status" };
    				JLabel[] valores = { lblId, lblProdutos, lblPT, lblTP, lblStatus };
    					for (int i = 0; i < rotulos.length; i++) {
    					g.gridx = 0; g.gridy = i; g.weightx = 0; g.fill = GridBagConstraints.NONE;
    						g.anchor = GridBagConstraints.EAST;
    						p.add(new JLabel(rotulos[i]), g);
    						valores[i].setFont(valores[i].getFont().deriveFont(Font.BOLD));
    						g.gridx = 1; g.weightx = 1; g.fill = GridBagConstraints.HORIZONTAL;
    						g.anchor = GridBagConstraints.WEST;
    						p.add(valores[i], g);
    					}
    					g.gridx = 0; g.gridy = rotulos.length; g.gridwidth = 2; g.weighty = 1;
    					g.fill = GridBagConstraints.BOTH;
    					p.add(new JPanel(), g); // empurra tudo para cima
    					return p;
    	}
    	
    	/** Estado "editando": o mesmo formulario serve para Novo e para Editar. */
    			private JPanel criarCartaoEditar() {
    				JPanel p = new JPanel(new GridBagLayout());
    				GridBagConstraints g = new GridBagConstraints();
    					g.insets = new Insets(5, 6, 5, 6);
    					g.fill = GridBagConstraints.HORIZONTAL;
    					String[] rotulos = {  "id:", "produtos", "preço total", "tipo de pagamnto", "status" };
    					JTextField[] campos = { txtid, txtprodutos, txtpreço , txtTp, txtStaus };
    						for (int i = 0; i < rotulos.length; i++) {
    							g.gridx = 0; g.gridy = i; g.weightx = 0; g.anchor = GridBagConstraints.EAST;
    							p.add(new JLabel(rotulos[i]), g);
    							g.gridx = 1; g.weightx = 1;
    							p.add(campos[i], g);
    	}
    						JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    						botoes.add(btnSalvar);
    						botoes.add(btnCancelar);
    							g.gridx = 0; g.gridy = rotulos.length; g.gridwidth = 2;
    							p.add(botoes, g);
    							g.gridy = rotulos.length + 1; g.weighty = 1; g.fill = GridBagConstraints.BOTH;
    							p.add(new JPanel(), g);
    							return p;
    	}
    			public void carregarTabela(List<Pedido> lista) {
    				modelo.setDados(lista);
    			}
    				public void mostrarVazio() {
    					painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
    						cardsDetalhe.show(painelDetalhe, "vazio");
    				}
    					public void mostrarDetalhe(Pedido ped) {
    						lblId.setText(ped.getidPedido());
    						lblProdutos.setText(ped.getEspecie());
    						lblPT.setText(String.format("%.1f kg", ped.getPeso()));
    						lblTP
    						lblStatus
    						lblDono.setText(a.getDono());
    						
    							painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
    								cardsDetalhe.show(painelDetalhe, "ver");
    						}
    							/** Recebe o animal a editar; null significa "animal novo". */
    								public void editarAnimal(Pedido ped) {
    									idEmEdicao = (ped == null) ? 0 : ped.getId();
    									txtNome.setText(ped == null ? "" : ped.getNome());
    									txtEspecie.setText(ped == null ? "" : ped.getEspecie());
    									txtPeso.setText(ped == null ? "" : String.valueOf(a.getPeso()));
    									txtDono.setText(ped == null ? "" : ped.getDono());
    										String titulo = (ped == null) ? "Novo pedido" : "Editar pedido";
    										painelDetalhe.setBorder(BorderFactory.createTitledBorder(titulo));
    										cardsDetalhe.show(painelDetalhe, "editar");
    										txtNome.requestFocusInWindow();
    						}
    								/** Um unico lugar decide o que esta habilitado, a partir do ESTADO da tela. */
    								public void atualizarBotoes(boolean temSelecao, boolean editando) {
    									btnNovo.setEnabled(!editando);
    									btnEditar.setEnabled(temSelecao && !editando);
    									btnExcluir.setEnabled(temSelecao && !editando);
    									tabela.setEnabled(!editando); // evita trocar de linha no meio da edicao
    									txtBusca.setEnabled(!editando);
    									cbEspecie.setEnabled(!editando);
    								}
    								public void setCarregando(boolean carregando) {
    									tabela.setEnabled(!carregando);
    									btnNovo.setEnabled(!carregando);
    										setCursor(carregando
    												? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
    														: Cursor.getDefaultCursor());
    										if (carregando) {
    											mostrarStatus("Carregando...");
    										}
    								}
    								/** Um painel por especie, criado em tempo de execucao (Parte 6.4 do material anterior). */
    								public void carregarResumo(Map<String, Integer> totais) {
    								painelResumo.removeAll(); // 1. limpa
    								for (Map.Entry<String, Integer> e : totais.entrySet()) { // 2. um por registro
    								JLabel numero = new JLabel(String.valueOf(e.getValue()), SwingConstants.CENTER);
    								numero.setFont(numero.getFont().deriveFont(Font.BOLD, 22f));
    								JLabel nome = new JLabel(e.getKey(), SwingConstants.CENTER);
    								JPanel card = new JPanel(new BorderLayout());
    								card.setBorder(BorderFactory.createCompoundBorder(
    								BorderFactory.createLineBorder(Color.LIGHT_GRAY),
    								BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    								card.add(numero, BorderLayout.CENTER);
    								card.add(nome, BorderLayout.SOUTH);
    								painelResumo.add(card);
    								}
    								painelResumo.revalidate(); // 3. recalcula
    								painelResumo.repaint(); // 4. redesenha
    								}
    								public Map<String, Integer> contarPorEspecie() {
    									Map<String, Integer> totais = new LinkedHashMap<>();
    									String sql = "SELECT especie, COUNT(*) AS total FROM animal "
    									+ "GROUP BY especie ORDER BY especie";
    									try (Connection con = abrir();
    									PreparedStatement ps = con.prepareStatement(sql);
    									ResultSet rs = ps.executeQuery()) {
    									while (rs.next()) {
    									totais.put(rs.getString("especie"), rs.getInt("total"));
    									}
    									} catch (SQLException ex) {
    									throw new RuntimeException(ex);
    									}
    									return totais;
    									}
    								/** Versao para consultas lentas: o banco roda em outra thread, a tela so e tocada na EDT. */
    								private void atualizarTelaAssincrono() {
    								final String especie = this.view.getEspecieSelecionada(); // le a tela AQUI, na EDT
    								final String busca = this.view.getBusca();
    								this.view.setCarregando(true);
    								new SwingWorker<List<Animal>, Void>() {
    								@Override
    								protected List<Animal> doInBackground() {
    								return bd.listarFiltrado(especie, busca); // NUNCA mexa na tela aqui
    								}
    								@Override
    								protected void done() { // de volta a EDT
    								try {
    								view.carregarTabela(get());
    								} catch (Exception ex) {
    								view.mostrarStatus("Erro ao consultar: " + ex.getMessage());
    								} finally {
    								viewp.setCarregando(false);
    								atualizarDetalhe();
    								}
    								}
    								}.execute();
    								}

    public void fechar() {
        dispose();
    }
}