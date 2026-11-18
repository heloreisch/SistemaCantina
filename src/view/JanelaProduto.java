package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;
import java.util.Map;

import model.Categoria;
import model.Produto;
import model.Status;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;


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
	private JComboBox<String> cbFiltroCategoria = new JComboBox<>();
	private JComboBox<Categoria> cbCategoriaCadastro = new JComboBox<>();
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
	
	// itens do menu
    private JMenuItem miLimpar;
    private JMenuItem miSair;
    private JMenuItem miManual;
    private JMenuItem miSobre;

	public JanelaProduto() {
		setTitle("Sistema de Cantina - Produtos");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(1200, 560); // mais larga
        setMinimumSize(new Dimension(1000, 500));
        setLocationRelativeTo(null);

        // icone da janela barra de titulo e de tarefas
        ImageIcon icone = carregarIcone("/img/logoCantina.png", 32, 32);
        if (icone != null) {
            setIconImage(icone.getImage());
        }

        // painel raiz
        JPanel raiz = new JPanel(new BorderLayout());
        setContentPane(raiz);
     
        // painel com o layout principal da tela
        JPanel conteudo = new JPanel(new BorderLayout(6, 6));
        raiz.add(conteudo, BorderLayout.CENTER);
        
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBorder(new EmptyBorder(0, 10, 0, 10));
        ImageIcon logo = carregarIcone("/img/logoCantina.png", 260, 140);
        if (logo != null) {
            lblLogo.setIcon(logo);
        } else {
            lblLogo.setText("Sistema de Cantina");
        }
        
        colorir(btnNovo,    new Color(46, 125, 50));   // verde
        colorir(btnExcluir, new Color(198, 40, 40));   // vermelho

        // comandos + resumo a esquerda e logo a direita
        painelResumo.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        JPanel esquerda = new JPanel(new BorderLayout());
        esquerda.add(criarBarraComandos(), BorderLayout.NORTH);
        esquerda.add(painelResumo, BorderLayout.CENTER);

        JPanel topo = new JPanel(new BorderLayout());
        topo.add(esquerda, BorderLayout.CENTER);
        topo.add(lblLogo, BorderLayout.EAST);
        conteudo.add(topo, BorderLayout.NORTH);

        tabela.setRowHeight(22);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            protected void setValue(Object v) {
                setText(v == null ? "" : String.format("R$ %.2f", (Double) v));
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
        });
        conteudo.add(new JScrollPane(tabela), BorderLayout.CENTER);

        conteudo.add(criarPainelDetalhe(), BorderLayout.EAST);
        conteudo.add(lblStatus, BorderLayout.SOUTH);

        mostrarVazio();
        atualizarBotoes(false, false);
        
     // ---------- o menu ----------
        setJMenuBar(criarMenu());

	}
	
	/**
	* Carrega uma imagem de dentro do projeto e ajusta ao tamanho pedido,
	* mantendo a proporcao. Devolve null se o arquivo nao for encontrado.
	*/
	private ImageIcon carregarIcone(String caminho, int largura, int altura) {
	URL url = JanelaProduto.class.getResource(caminho);
	if (url == null) {
	System.err.println("Imagem nao encontrada: " + caminho);
	return null;
	}
	ImageIcon original = new ImageIcon(url);
	double escala = Math.min((double) largura / original.getIconWidth(),
	(double) altura / original.getIconHeight());
	int w = (int) (original.getIconWidth() * escala);
	int h = (int) (original.getIconHeight() * escala);
	Image reduzida = original.getImage()
	.getScaledInstance(w, h, Image.SCALE_SMOOTH);
	return new ImageIcon(reduzida);
	}

	
	/** Monta a barra de menus: Arquivo e Ajuda. */
    private JMenuBar criarMenu() {
        JMenuBar barra = new JMenuBar();

        // ---- menu Arquivo ----
        JMenu menuArquivo = new JMenu("Arquivo");
        menuArquivo.setMnemonic(KeyEvent.VK_A); // Alt+A abre o menu
        miLimpar = new JMenuItem("Limpar campos");
        miLimpar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        menuArquivo.add(miLimpar);
        menuArquivo.addSeparator(); // linha divisoria
        miSair = new JMenuItem("Sair");
        miSair.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        menuArquivo.add(miSair);

        // ---- menu Ajuda ----
        JMenu menuAjuda = new JMenu("Ajuda");
        menuAjuda.setMnemonic(KeyEvent.VK_J);
        miManual = new JMenuItem("Manual de uso");
        miManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuAjuda.add(miManual);
        miSobre = new JMenuItem("Sobre");
        menuAjuda.add(miSobre);

        barra.add(menuArquivo);
        barra.add(menuAjuda);
        return barra;
    }

	private JPanel criarBarraComandos() {
		JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
		barra.add(btnNovo);
		barra.add(btnEditar);
		barra.add(btnExcluir);
		barra.add(Box.createHorizontalStrut(16));
		barra.add(new JLabel("Busca:"));
		barra.add(txtBusca);
		barra.add(new JLabel("Categoria:"));
		barra.add(cbFiltroCategoria);
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
		JLabel msg = new JLabel("<html><center>Selecione um produto<br>na tabela.</center></html>",
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

		String[] rotulos = { "Nome:", "Categoria:", "Preço:" };
		JLabel[] valores = { lblNome, lblCategoria, lblPreco };
		for (int i = 0; i < rotulos.length; i++) {
			g.gridx = 0;
			g.gridy = i;
			g.weightx = 0;
			g.fill = GridBagConstraints.NONE;
			g.anchor = GridBagConstraints.EAST;
			p.add(new JLabel(rotulos[i]), g);

			valores[i].setFont(valores[i].getFont().deriveFont(Font.BOLD));
			g.gridx = 1;
			g.weightx = 1;
			g.fill = GridBagConstraints.HORIZONTAL;
			g.anchor = GridBagConstraints.WEST;
			p.add(valores[i], g);
		}
		g.gridx = 0;
		g.gridy = rotulos.length;
		g.gridwidth = 2;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		p.add(new JPanel(), g);
		return p;
	}

	private JPanel criarCartaoEditar() {
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(5, 6, 5, 6);
		g.fill = GridBagConstraints.HORIZONTAL;

		String[] rotulos = { "Nome:", "Categoria:", "Preço (R$):" };
		JComponent[] campos = { txtNome, cbCategoriaCadastro, txtPreco };

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
		botoes.add(btnCancelar);
		g.gridx = 0;
		g.gridy = rotulos.length;
		g.gridwidth = 2;
		p.add(botoes, g);

		g.gridy = rotulos.length + 1;
		g.weighty = 1;
		g.fill = GridBagConstraints.BOTH;
		p.add(new JPanel(), g);
		return p;
	}

	public void carregarTabela(List<Produto> lista) {
		modelo.setDados(lista);
	}

	public void mostrarVazio() {
		painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
		cardsDetalhe.show(painelDetalhe, "vazio");
	}

	public void mostrarDetalhe(Produto a) {
		lblNome.setText(a.getNome());
		lblCategoria.setText(a.getCategoria().toString());
		lblPreco.setText(String.format("R$ %.2f ", a.getPreco()));

		painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
		cardsDetalhe.show(painelDetalhe, "ver");
	}

	public void editarProduto(Produto p) {
		idEmEdicao = (p == null) ? 0 : p.getId();

		txtNome.setText(p == null ? "" : p.getNome());

		cbCategoriaCadastro.setSelectedItem(p == null ? null : p.getCategoria());

		txtPreco.setText(p == null ? "" : String.valueOf(p.getPreco()));

		String titulo = (p == null) ? "Novo produto" : "Editar produto";

		painelDetalhe.setBorder(BorderFactory.createTitledBorder(titulo));
		cardsDetalhe.show(painelDetalhe, "editar");

		txtNome.requestFocusInWindow();
	}

	public Produto lerFormulario() {
		double preco = Double.parseDouble(txtPreco.getText().trim().replace(',', '.'));

		// Obtém o objeto Categoria selecionado no JComboBox
		Categoria categoria = (Categoria) cbCategoriaCadastro.getSelectedItem();

		Produto p = new Produto(txtNome.getText().trim(), preco, categoria);

		p.setId(idEmEdicao);

		return p;

	}

	public Produto getProdutoSelecionado() {
		int linhaTela = tabela.getSelectedRow();
		if (linhaTela < 0)
			return null;
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
		cbFiltroCategoria.removeAllItems();
		cbCategoriaCadastro.removeAllItems();

		cbFiltroCategoria.addItem("(todas)");

		for (String e : categorias) {
			cbFiltroCategoria.addItem(e);
			cbCategoriaCadastro.addItem(Categoria.valueOf(e));
		}
		if (manter != null)
			cbFiltroCategoria.setSelectedItem(manter);
	}

	public void carregarResumo(Map<String, Integer> totais) {
		painelResumo.removeAll();
		for (Map.Entry<String, Integer> e : totais.entrySet()) {
			JLabel numero = new JLabel(String.valueOf(e.getValue()), SwingConstants.CENTER);
			numero.setFont(numero.getFont().deriveFont(Font.BOLD, 22f));
			JLabel nome = new JLabel(e.getKey(), SwingConstants.CENTER);

			JPanel card = new JPanel(new BorderLayout());
			card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
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
		cbFiltroCategoria.setEnabled(!editando);
		cbCategoriaCadastro.setEnabled(editando);
	}

	public void setCarregando(boolean carregando) {
		tabela.setEnabled(!carregando);
		btnNovo.setEnabled(!carregando);
		setCursor(carregando ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
		if (carregando)
			mostrarStatus("Carregando...");
	}
	public void limparFormulario() {
		txtNome.setText("");
        txtPreco.setText("");
        txtBusca.setText("");
        cbFiltroCategoria.setSelectedIndex(-1);
        cbCategoriaCadastro.setSelectedIndex(-1);
        
    }
	
	public void limparSelecao() {
		tabela.clearSelection();
	}
	
	public void limparBusca() {
        txtBusca.setText("");
        cbFiltroCategoria.setSelectedIndex(0);
        tabela.clearSelection();
    }
	
    public void fechar() { dispose(); }
    
	public void mostrarStatus(String texto) {
		lblStatus.setText(" " + texto);
	}

	public String getBusca() {
		return txtBusca.getText().trim();
	}

	public String getCategoriaSelecionada() {
		Object sel = cbFiltroCategoria.getSelectedItem();
		return (sel == null) ? "" : sel.toString();
	}

	public boolean confirmar(String pergunta) {
		return JOptionPane.showConfirmDialog(this, pergunta, "Confirmar",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}


	// getters usados pelo Controller
	public JButton getBtnNovo() {
		return btnNovo;
	}

	public JButton getBtnEditar() {
		return btnEditar;
	}

	public JButton getBtnExcluir() {
		return btnExcluir;
	}

	public JButton getBtnSalvar() {
		return btnSalvar;
	}

	public JButton getBtnCancelar() {
		return btnCancelar;
	}

	public JTextField getTxtBusca() {
		return txtBusca;
	}

	public JComboBox<String> getCbCategoria() {
		return cbFiltroCategoria;
	}

	public JTable getTabela() {
		return tabela;
	}
	
	 // colorir os botoes
    private void colorir(JButton botao, Color fundo) {
        botao.setBackground(fundo);
        botao.setForeground(Color.WHITE);
        botao.setOpaque(true);
        botao.setContentAreaFilled(true);
        botao.setFocusPainted(false);
    }


	// ---- Grupo 1: entregar componentes ao Controller ----
	public JMenuItem getMiLimpar() { return miLimpar; }
	public JMenuItem getMiSair() { return miSair; }
	public JMenuItem getMiManual() { return miManual; }
	public JMenuItem getMiSobre() { return miSobre; }
	
	// ---- Grupo 2: capacidades da propria tela ----
	public void mostrarSobre() {
	new JanelaSobre(this).setVisible(true);
	}
	public void mostrarManual() {
	new JanelaManual(this).setVisible(true);
	}
	
	

}

