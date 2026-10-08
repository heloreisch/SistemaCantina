package view;

import javax.swing.*;
import java.awt.*;

import model.Categoria;

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

	// O gerenciador precisa ser atributo: e nele que chamamos show()
	// depois, para trocar de cartao.
	private CardLayout cards = new CardLayout();
	private JPanel painelCards = new JPanel(cards);
	private JButton btnIrCadastro = new JButton("Cadastro");
	private JButton btnIrLista = new JButton("Consulta");

	// Cartão 1
	private JTextField txtNome;
	private JTextField txtPreco;
	private JButton btnSalvar = new JButton("Salvar");

	// cartao 2
	private JComboBox<Categoria> comboCategoria = new JComboBox<>();
	private JButton btnFiltrar = new JButton("Filtrar");
	private ProdutoTableModel modelo = new ProdutoTableModel();
	private JTable tabela = new JTable(modelo);
	private JLabel lblStatus = new JLabel(" Pronto.");

	// Os botoes tambem sao atributos: o Controller precisa alcanca-los
	// para pendurar o ouvinte.
//    private JButton btnCadastrar;
//    private JButton btnLimpar;
//    private JButton btnFechar;

	public JanelaProduto() {

		setTitle("Cadastro de Produtos - Cantina");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 400);
		setMinimumSize(new Dimension(520, 320)); // impede encolher demais
		setLayout(new BorderLayout(6, 6));

		add(criarBarraNavegacao(), BorderLayout.NORTH);

		painelCards.add(criarCartaoCadastro(), "cadastro");
		painelCards.add(criarCartaoLista(), "lista");
		add(painelCards, BorderLayout.CENTER);
		add(lblStatus, BorderLayout.SOUTH);
	}

	public void carregarCategorias(List<Categoria> categorias) {
	    comboCategoria.removeAllItems();

	    for (Categoria categoria : categorias) {
	        comboCategoria.addItem(categoria);
	    }
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
		String[] rotulos = { "Nome:", "Categoria:", "Preço (R$):"};
		JComponent[] campos = { txtNome, txtPreco};
		for (int i = 0; i < rotulos.length; i++) {
			g.gridx = 0;
			g.gridy = i;
			g.weightx = 0;
			g.anchor = GridBagConstraints.EAST;
			p.add(new JLabel(rotulos[i]), g);
			g.gridx = 1;
			g.weightx = 1;
			p.add(campos[i], g);
		}
		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
		botoes.add(btnSalvar);
		g.gridx = 0;
		g.gridy = rotulos.length;
		g.gridwidth = 2;
		g.weightx = 1;
		p.add(botoes, g);
		// Empurra tudo para cima: uma celula vazia que fica com a sobra
		// vertical. Sem ela, o formulario se espalha pelo painel inteiro.
		g.gridy = rotulos.length + 1;
		g.weighty = 1;
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
	 filtro.add(cbCategoria);
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
	 public JComboBox<String> getCategoria() { return cbCategoria; }
	 public JTable getTabela() { return tabela; }

	// ---- Grupo 2: capacidades da tela ---------------------------------
	 /** Troca o cartao visivel. O nome tem que ser o mesmo do add(). */
	 public void mostrarCartao(String nome) {
	 cards.show(painelCards, nome);
	 }
	 /** Recebe a lista pronta e manda o modelo se redesenhar. */
	 public void carregarTabela(java.util.List<model.Produto> lista) {
	 modelo.setDados(lista);
	 }
	 /** Preenche o combo com o que veio do banco. */
	 public void carregarCategorias(java.util.List<String> categoria) {
	 cbCategoria.removeAllItems();
	 cbCategoria.addItem("(todas)");
	 for (String e : categoria) {
	 cbCategoria.addItem(e);
	 }
	 }
	 public String getEspecieSelecionada() {
	 Object sel = cbCategoria.getSelectedItem();
	 return (sel == null) ? "" : sel.toString();
	 }
	 public void mostrarStatus(String texto) {
	 lblStatus.setText(" " + texto);
	 }
	}

	