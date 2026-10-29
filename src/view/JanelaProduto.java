package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

import model.Categoria;
import model.Produto;

/**
 * VIEW - a tela. PASSO 7 do roteiro.
 *
 * Um par rotulo + campo por atributo do Model, e os botoes.
 *
 * Dois grupos de metodos, e nada alem disso: Grupo 1 - get... devolvendo
 * componentes ao Controller; Grupo 2 - metodos que exibem ou fazem o que
 * mandarem.
 *
 * Confira com Ctrl+F: nao ha "addActionListener" e nao ha "if" aqui.
 */
public class JanelaProduto extends JFrame {

	private static final long serialVersionUID = 1L;
	
    // CENTER: a lista
    private ProdutoTableModel modelo = new ProdutoTableModel();
    private JTable tabela = new JTable(modelo);

    // NORTH: comandos, busca, filtro e resumo
    private JButton btnNovo = new JButton("Novo");
    private JButton btnEditar = new JButton("Editar");
    private JButton btnExcluir = new JButton("Excluir");
    private JTextField txtBusca = new JTextField(14);
    private JComboBox<String> cbCategorias = new JComboBox<>();
    private JPanel painelResumo = new JPanel(new GridLayout(1, 0, 8, 0));

    // EAST: detalhes com tres cartoes
    private CardLayout cardsDetalhe = new CardLayout();
    private JPanel painelDetalhe = new JPanel(cardsDetalhe);
    private JLabel lblNome = new JLabel(), lblCategoria = new JLabel();
    private JLabel lblPreco = new JLabel();
    private JTextField txtNome = new JTextField();
    private JTextField txtPreco = new JTextField();
    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnCancelar = new JButton("Cancelar");
    private int idEmEdicao = 0; // 0 = animal novo

    // SOUTH
    private JLabel lblStatus = new JLabel(" Pronto.");
    
//	// O gerenciador precisa ser atributo: e nele que chamamos show()
//	// depois, para trocar de cartao.
//	private CardLayout cards = new CardLayout();
//	private JPanel painelCards = new JPanel(cards);
//	private JButton btnIrCadastro = new JButton("Cadastro");
//	private JButton btnIrLista = new JButton("Consulta");
//
//	// Cartão 1 (cadastro)
//		private JTextField txtNome = new JTextField(20);
//		private JTextField txtPreco = new JTextField(10);
//		private JComboBox<Categoria> cbCadastro = new JComboBox<>();
//		private JButton btnSalvar = new JButton("Salvar");
//
//		// Cartão 2 (consulta)
//		private JComboBox<Object> cbFiltro = new JComboBox<>();
//		private JButton btnFiltrar = new JButton("Filtrar");
//		private ProdutoTableModel modelo = new ProdutoTableModel();
//		private JTable tabela = new JTable(modelo);
//		private JLabel lblStatus = new JLabel(" Pronto.");

	// Os botoes tambem sao atributos: o Controller precisa alcanca-los
	// para pendurar o ouvinte.
//    private JButton btnCadastrar;
//    private JButton btnLimpar;
//    private JButton btnFechar;

    public JanelaProduto() {
        setTitle("Sistema da cantina");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 470);
        setMinimumSize(new Dimension(700, 380));
        setLayout(new BorderLayout(6, 6));

        painelResumo.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(criarBarraComandos(), BorderLayout.NORTH);
        topo.add(painelResumo, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

        tabela.setRowHeight(22);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(criarPainelDetalhe(), BorderLayout.EAST);
        add(lblStatus, BorderLayout.SOUTH);

        mostrarVazio();
        atualizarBotoes(false, false);
    }
    
//	public JanelaProduto() {
//
//		setTitle("Cadastro de Produtos - Cantina");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setSize(640, 400);
//		setMinimumSize(new Dimension(520, 320)); // impede encolher demais
//		setLayout(new BorderLayout(6, 6));
//
//		add(criarBarraNavegacao(), BorderLayout.NORTH);
//
//		painelCards.add(criarCartaoCadastro(), "cadastro");
//		painelCards.add(criarCartaoLista(), "lista");
//		add(painelCards, BorderLayout.CENTER);
//		add(lblStatus, BorderLayout.SOUTH);
//	}

	
	
    private JPanel criarBarraComandos() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        barra.add(btnNovo);
        barra.add(btnEditar);
        barra.add(btnExcluir);
        barra.add(Box.createHorizontalStrut(16));
        barra.add(new JLabel("Busca:"));
        barra.add(txtBusca);
        barra.add(new JLabel("Especie:"));
        barra.add(cbCategorias);
        return barra;
    }
    private JPanel criarPainelDetalhe() {
        painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        painelDetalhe.setPreferredSize(new Dimension(250, 0));
        painelDetalhe.add(criarCartaoVazio(), "vazio");
        painelDetalhe.add(criarCartaoVer(), "ver");
        painelDetalhe.add(criarCartaoEditar(), "editar");
        return painelDetalhe;
    }
    
    private JPanel criarCartaoVazio() {
        JLabel msg = new JLabel("<html><center>Selecione um animal<br>na tabela.</center></html>",
                SwingConstants.CENTER);
        msg.setForeground(Color.GRAY);
        JPanel p = new JPanel(new BorderLayout());
        p.add(msg, BorderLayout.CENTER);
        return p;
    }

    private JPanel criarCartaoVer() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);

        String[] rotulos = { "Nome:", "Categoria:", "Preço:"};
        JLabel[] valores = { lblNome, lblCategoria, lblPreco };
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
        p.add(new JPanel(), g);
        return p;
    }
    
    private JPanel criarCartaoEditar() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 6, 5, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        String[] rotulos = { "Nome:", "Categoria:", "Peso (kg):"};
        JComponent[] campos = { txtNome, cbCategorias, txtPreco };
        
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
    
    public void carregarTabela(List<Produto> lista) { modelo.setDados(lista); }

    public void mostrarVazio() {
        painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        cardsDetalhe.show(painelDetalhe, "vazio");
    }
    
    public void mostrarDetalhe(Produto a) {
        lblNome.setText(a.getNome());
        cbCategorias.setSelectedItem(a.getCategoria());
        lblPreco.setText(String.format("R$ %.2f ", a.getPreco()));
        painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        cardsDetalhe.show(painelDetalhe, "ver");
    }
    
    public void editarAnimal(Produto a) {
        idEmEdicao = (a == null) ? 0 : a.getId();
        txtNome.setText(a == null ? "" : a.getNome());
        cbCategorias.setSelectedItem(a == null ? "" : a.getCategoria());
        txtPreco.setText(a == null ? "" : String.valueOf(a.getPreco()));
        String titulo = (a == null) ? "Novo animal" : "Editar animal";
        painelDetalhe.setBorder(BorderFactory.createTitledBorder(titulo));
        cardsDetalhe.show(painelDetalhe, "editar");
        txtNome.requestFocusInWindow();
    }
    
    public Produto lerFormulario() {
        double preco = Double.parseDouble(txtPreco.getText().trim().replace(',', '.'));
        
        // Obtém o objeto Categoria selecionado no JComboBox
        Categoria categoria = (Categoria) cbCategorias.getSelectedItem();

        return new Produto(txtNome.getText().trim(), preco, categoria);
    }

    public Produto getAnimalSelecionado() {
        int linhaTela = tabela.getSelectedRow();
        if (linhaTela < 0) return null;
        int linhaModelo = tabela.convertRowIndexToModel(linhaTela);
        return modelo.getProduto(linhaModelo);
    }
    
    public void selecionarPorId(int id) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getProduto(i).getId() == id) {
                int linhaTela = tabela.convertRowIndexToView(i);
                tabela.setRowSelectionInterval(linhaTela, linhaTela);
                tabela.scrollRectToVisible(tabela.getCellRect(linhaTela, 0, true));
                return;
            }
        }
        tabela.clearSelection();
    }
    
    public void carregarCategorias(List<String> categorias, String manter) {
        cbCategorias.removeAllItems();
        cbCategorias.addItem("(todas)");
        for (String e : categorias) cbCategorias.addItem(e);
        if (manter != null) cbCategorias.setSelectedItem(manter);
    }
    
    public void carregarResumo(Map<String, Integer> totais) {
        painelResumo.removeAll();
        for (Map.Entry<String, Integer> e : totais.entrySet()) {
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
        painelResumo.revalidate();
        painelResumo.repaint();
    }

    public void atualizarBotoes(boolean temSelecao, boolean editando) {
        btnNovo.setEnabled(!editando);
        btnEditar.setEnabled(temSelecao && !editando);
        btnExcluir.setEnabled(temSelecao && !editando);
        tabela.setEnabled(!editando);
        txtBusca.setEnabled(!editando);
        cbCategorias.setEnabled(!editando);
    }
    
    public void setCarregando(boolean carregando) {
        tabela.setEnabled(!carregando);
        btnNovo.setEnabled(!carregando);
        setCursor(carregando
                ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                : Cursor.getDefaultCursor());
        if (carregando) mostrarStatus("Carregando...");
    }
    
    public void mostrarStatus(String texto) { lblStatus.setText(" " + texto); }
    public String getBusca() { return txtBusca.getText().trim(); }
    
    public String getEspecieSelecionada() {
        Object sel = cbCategorias.getSelectedItem();
        return (sel == null) ? "" : sel.toString();
    }
    public boolean confirmar(String pergunta) {
        return JOptionPane.showConfirmDialog(this, pergunta, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public void limparSelecao() { tabela.clearSelection(); }

    // getters usados pelo Controller
    public JButton getBtnNovo() { return btnNovo; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnExcluir() { return btnExcluir; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JTextField getTxtBusca() { return txtBusca; }
    public JComboBox<String> getCbEspecie() { return cbCategorias; }
    public JTable getTabela() { return tabela; }
}

//	/** Cartao 1: formulario com GridBagLayout. */
//	private JPanel criarCartaoCadastro() {
//		JPanel p = new JPanel(new GridBagLayout());
//		p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//		GridBagConstraints g = new GridBagConstraints();
//		
//		g.insets = new Insets(5, 6, 5, 6);
//		g.fill = GridBagConstraints.HORIZONTAL;
//		
//		String[] rotulos = { "Nome:", "Categoria:", "Preço (R$):"};
//		JComponent[] campos = { txtNome, cbCadastro, txtPreco};
//		
//		for (int i = 0; i < rotulos.length; i++) {
//			g.gridx = 0;
//			g.gridy = i;
//			g.weightx = 0;
//			g.anchor = GridBagConstraints.EAST;
//			p.add(new JLabel(rotulos[i]), g);
//			g.gridx = 1;
//			g.weightx = 1;
//			p.add(campos[i], g);
//		}
//		
//		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
//		botoes.add(btnSalvar);
//		g.gridx = 0;
//		g.gridy = rotulos.length;
//		g.gridwidth = 2;
//		g.weightx = 1;
//		p.add(botoes, g);
//		// Empurra tudo para cima: uma celula vazia que fica com a sobra
//		// vertical. Sem ela, o formulario se espalha pelo painel inteiro.
//		g.gridy = rotulos.length + 1;
//		g.weighty = 1;
//		g.fill = GridBagConstraints.BOTH;
//		p.add(new JPanel(), g);
//		return p;
//	}
//
//	/** Cartao 2: filtro + tabela, com BorderLayout. */
//	 private JPanel criarCartaoLista() {
//	 JPanel p = new JPanel(new BorderLayout(6, 6));
//	 p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
//	 JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
//	 filtro.add(new JLabel("Categoria:"));
//	 filtro.add(new JLabel("Especie:"));
//	 filtro.add(cbFiltro);
//	 filtro.add(btnFiltrar);
//	 p.add(filtro, BorderLayout.NORTH);
//	 tabela.setRowHeight(22);
//	 tabela.setAutoCreateRowSorter(true); // clicar no cabecalho ordena
//	 p.add(new JScrollPane(tabela), BorderLayout.CENTER);
//	 return p;
//	 }
//	 
////	// ---- Grupo 1: componentes para o Controller -----------------------
////	 public JButton getBtnIrCadastro() { return btnIrCadastro; }
////	 public JButton getBtnIrLista() { return btnIrLista; }
////	 public JButton getBtnFiltrar() { return btnFiltrar; }
////	 public JButton getBtnSalvar() { return btnSalvar; }
////	 public JTextField getTxtNome() { return txtNome; }
////	 public JTextField getTxtPreco() { return txtPreco; }
////	 public JTable getTabela() { return tabela; }
////
////	 /** Categoria escolhida no cadastro (já como enum). */
////		public Categoria getCategoriaCadastro() {
////			return (Categoria) cbCadastro.getSelectedItem();
//		}
//		
//		/** Categoria do filtro, ou null se estiver em "(todas)". */
//		public Categoria getCategoriaFiltro() {
//			Object sel = cbFiltro.getSelectedItem();
//			return (sel instanceof Categoria) ? (Categoria) sel : null;
//		}
//		
//	// ---- Grupo 2: capacidades da tela ---------------------------------
//	 /** Troca o cartao visivel. O nome tem que ser o mesmo do add(). */
//	 public void mostrarCartao(String nome) {
//	 cards.show(painelCards, nome);
//	 }
//	 /** Recebe a lista pronta e manda o modelo se redesenhar. */
//	 public void carregarTabela(List<Produto> lista) {
//	 modelo.setDados(lista);
//	 }
//	 
//	 public void carregarCadastro(List<Categoria> categorias) {
//		    cbCadastro.removeAllItems();
//		    for (Categoria c : categorias) {
//		        cbCadastro.addItem(c);
//		    }
//		}
//	 
//	 public void carregarFiltro(List<Categoria> categorias) {
//			cbFiltro.removeAllItems();
//			cbFiltro.addItem("(todas)");
//			for (Categoria c : categorias) {
//				cbFiltro.addItem(c);
//			}
//		}
//	 
//	 public void limparCampos() {
//			txtNome.setText("");
//			txtPreco.setText("");
//			if (cbCadastro.getItemCount() > 0) {
//				cbCadastro.setSelectedIndex(0);
//			}
//			txtNome.requestFocus();
//		}
//
//	 public void mostrarErro(String msg) {
//			JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
//		}
//
//		public void mostrarMensagem(String msg) {
//			JOptionPane.showMessageDialog(this, msg);
//		}
//
//		public void mostrarStatus(String texto) {
//			lblStatus.setText(" " + texto);
//		}
//	}
//	

	