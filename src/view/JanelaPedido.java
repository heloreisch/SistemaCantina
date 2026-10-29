package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import model.Pagamento;
import model.Pedido;
import model.Produto;
import model.Status;
import view.PedidoTableModel;

/** VIEW - so desenha. Nao conhece banco nem regra de negocio. */
public class JanelaPedido extends JFrame {
    private static final long serialVersionUID = 1L;

    // CENTER: a lista
    private PedidoTableModel modelo = new PedidoTableModel();
    private JTable tabela = new JTable(modelo);

    // NORTH: comandos, busca, filtro e resumo
    private JButton btnNovo = new JButton("Novo");
    private JButton btnEditar = new JButton("Editar");
    private JButton btnExcluir = new JButton("Excluir");
    private JTextField txtBusca = new JTextField(14);
    private JComboBox<String> cbFiltroStatus = new JComboBox<>();
    private JPanel painelResumo = new JPanel(new GridLayout(1, 0, 8, 0));

    // EAST: detalhes com tres cartoes
    private CardLayout cardsDetalhe = new CardLayout();
    private JPanel painelDetalhe = new JPanel(cardsDetalhe);
    private JLabel lblDId = new JLabel(), lblDProdutos = new JLabel(), lblDPreco = new JLabel();
    private JLabel lblDPagamento = new JLabel(), lblDStatus = new JLabel();

    private JLabel lblEId = new JLabel("(novo)");
    private JComboBox<Produto> cbProduto = new JComboBox<>();
    private DefaultListModel<Produto> itensModel = new DefaultListModel<>();
    private JList<Produto> listaItens = new JList<>(itensModel);
    private JButton btnAdicionar = new JButton("Adicionar");
    private JButton btnRemoverItem = new JButton("Remover");
    private JTextField txtPrecoTotal = new JTextField();
    private JComboBox<Pagamento> cbPagamento = new JComboBox<>(Pagamento.values());
    private JComboBox<Status> cbStatus = new JComboBox<>(Status.values());
    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnCancelar = new JButton("Cancelar");
    private int idEmEdicao = 0; // 0 = pedido novo

    // SOUTH
    private JLabel lblBarra = new JLabel(" Pronto.");

    public JanelaPedido() {
        setTitle("Sistema de Cantina - Pedidos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 470);
        setMinimumSize(new Dimension(760, 380));
        setLayout(new BorderLayout(6, 6));

        cbFiltroStatus.addItem("(todos)");
        for (Status s : Status.values()) cbFiltroStatus.addItem(s.name());
        cbProduto.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object v, int i, boolean sel, boolean foco) {
                super.getListCellRendererComponent(list, v, i, sel, foco);
                if (v instanceof Produto) {
                    Produto p = (Produto) v;
                    setText(p.getNome() + " - R$ " + String.format("%.2f", p.getPreco()));
                } else {
                    setText("(escolha um produto)");
                }
                return this;
            }
        });

        listaItens.setCellRenderer(cbProduto.getRenderer());
        painelResumo.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        JPanel topo = new JPanel(new BorderLayout());
        topo.add(criarBarraComandos(), BorderLayout.NORTH);
        topo.add(painelResumo, BorderLayout.CENTER);
        add(topo, BorderLayout.NORTH);

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
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(criarPainelDetalhe(), BorderLayout.EAST);
        add(lblBarra, BorderLayout.SOUTH);

        mostrarVazio();
        atualizarBotoes(false, false);
    }

    private JPanel criarBarraComandos() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        barra.add(btnNovo);
        barra.add(btnEditar);
        barra.add(btnExcluir);
        barra.add(Box.createHorizontalStrut(16));
        barra.add(new JLabel("Busca (id ou produto):"));
        barra.add(txtBusca);
        barra.add(new JLabel("Status:"));
        barra.add(cbFiltroStatus);
        return barra;
    }

    private JPanel criarPainelDetalhe() {
        painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        painelDetalhe.setPreferredSize(new Dimension(320, 0));
        painelDetalhe.add(criarCartaoVazio(), "vazio");
        painelDetalhe.add(criarCartaoVer(), "ver");
        painelDetalhe.add(criarCartaoEditar(), "editar");
        return painelDetalhe;
    }

    private JPanel criarCartaoVazio() {
        JLabel msg = new JLabel("<html><center>Selecione um pedido<br>na tabela.</center></html>",
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

        String[] rotulos = { "ID:", "Produtos:", "Preço total:", "Pagamento:", "Status:" };
        JLabel[] valores = { lblDId, lblDProdutos, lblDPreco, lblDPagamento, lblDStatus };
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
        g.insets = new Insets(4, 6, 4, 6);
        g.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        linha(p, g, y++, "ID:", lblEId);
        linha(p, g, y++, "Produto:", cbProduto);

        JPanel botoesItem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botoesItem.add(btnAdicionar);
        botoesItem.add(btnRemoverItem);
        g.gridx = 0; g.gridy = y++; g.gridwidth = 2; g.weightx = 1;
        p.add(botoesItem, g);

        JScrollPane rolagem = new JScrollPane(listaItens);
        rolagem.setBorder(BorderFactory.createTitledBorder("Itens do pedido"));
        rolagem.setPreferredSize(new Dimension(10, 110));
        g.gridx = 0; g.gridy = y++; g.gridwidth = 2; g.weightx = 1;
        p.add(rolagem, g);

        linha(p, g, y++, "Preço total:", txtPrecoTotal);
        linha(p, g, y++, "Pagamento:", cbPagamento);
        linha(p, g, y++, "Status:", cbStatus);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        botoes.add(btnSalvar);
        botoes.add(btnCancelar);
        g.gridx = 0; g.gridy = y++; g.gridwidth = 2;
        p.add(botoes, g);

        g.gridy = y; g.weighty = 1; g.fill = GridBagConstraints.BOTH;
        p.add(new JPanel(), g);
        return p;
    }

    /** Uma linha "rotulo + campo" do formulario. */
    private void linha(JPanel p, GridBagConstraints g, int y, String rotulo, Component campo) {
        g.gridwidth = 1;
        g.gridx = 0; g.gridy = y; g.weightx = 0; g.anchor = GridBagConstraints.EAST;
        p.add(new JLabel(rotulo), g);
        g.gridx = 1; g.weightx = 1;
        p.add(campo, g);
    }

    // ---- Grupo 2: capacidades da tela ----

    public void carregarTabela(List<Pedido> lista) { modelo.setDados(lista); }

    public void mostrarVazio() {
        painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        cardsDetalhe.show(painelDetalhe, "vazio");
    }

    public void mostrarDetalhe(Pedido ped) {
        lblDId.setText(String.valueOf(ped.getIdPedido()));
        lblDProdutos.setText(ped.getProdutos().isEmpty()
                ? "(sem itens)" : "<html>" + PedidoTableModel.nomesProdutos(ped.getProdutos()) + "</html>");
        lblDPreco.setText(String.format("R$ %.2f", ped.getPrecoTotal()));
        lblDPagamento.setText(ped.getTipo_pagamento().name());
        lblDStatus.setText(ped.getStatus_pedido().name());
        painelDetalhe.setBorder(BorderFactory.createTitledBorder("Detalhes"));
        cardsDetalhe.show(painelDetalhe, "ver");
    }

    /** Recebe o pedido a editar; null significa "pedido novo". */
    public void editarPedido(Pedido ped) {
        idEmEdicao = (ped == null) ? 0 : ped.getIdPedido();
        lblEId.setText(ped == null ? "(novo)" : String.valueOf(ped.getIdPedido()));
        cbProduto.setSelectedIndex(-1);
        itensModel.clear();
        if (ped != null) {
            for (Produto prod : ped.getProdutos()) {
                itensModel.addElement(prod);
            }
        }
        cbPagamento.setSelectedIndex(ped == null ? 0 : ped.getTipo_pagamento().ordinal());
        cbStatus.setSelectedIndex(ped == null ? 0 : ped.getStatus_pedido().ordinal());
        painelDetalhe.setBorder(BorderFactory.createTitledBorder(ped == null ? "Novo pedido" : "Editar pedido"));
        cardsDetalhe.show(painelDetalhe, "editar");
        txtPrecoTotal.requestFocusInWindow();
    }

    /** Lanca NumberFormatException se o preco for invalido (o Controller trata). Produtos: Controller. */
    public Pedido lerFormulario() {
        double preco = Double.parseDouble(txtPrecoTotal.getText().trim().replace(',', '.'));
        Pedido p = new Pedido();
        p.setIdPedido(idEmEdicao);
        p.setPrecoTotal(preco);
        p.setStatus_pedido((Status) cbStatus.getSelectedItem());
        p.setTipo_pagamento((Pagamento) cbPagamento.getSelectedItem());
        return p;
    }

    /** Preenche o combo com os produtos que vieram do banco. */
    public void carregarProdutos(List<Produto> produtos) {
        cbProduto.removeAllItems();
        for (Produto p : produtos) {
            cbProduto.addItem(p);
        }
        cbProduto.setSelectedIndex(-1);
    }

    /** Adiciona o produto a lista de itens. false se ele ja estiver la (compara pelo nome). */
    public boolean adicionarItem(Produto novo) {
        for (int i = 0; i < itensModel.size(); i++) {
            if (itensModel.get(i).getNome().equals(novo.getNome())) {
                return false;
            }
        }
        itensModel.addElement(novo);
        return true;
    }

    /** Remove da lista o item selecionado. false se nao havia nenhum selecionado. */
    public boolean removerItemSelecionado() {
        int i = listaItens.getSelectedIndex();
        if (i < 0) {
            return false;
        }
        itensModel.remove(i);
        return true;
    }

    public java.util.ArrayList<Produto> getItens() {
        java.util.ArrayList<Produto> r = new java.util.ArrayList<>();
        for (int i = 0; i < itensModel.size(); i++) {
            r.add(itensModel.get(i));
        }
        return r;
    }

    /** Soma dos precos dos itens, arredondada em centavos. */
    public double somarItens() {
        double soma = 0;
        for (int i = 0; i < itensModel.size(); i++) {
            soma += itensModel.get(i).getPreco();
        }
        return Math.round(soma * 100) / 100.0;
    }

    public Produto getProdutoSelecionado() {
        return (Produto) cbProduto.getSelectedItem();
    }

    public void setPrecoTotal(double valor) {
        txtPrecoTotal.setText(String.valueOf(valor));
    }

    public Pedido getPedidoSelecionado() {
        int linhaTela = tabela.getSelectedRow();
        if (linhaTela < 0) return null;
        int linhaModelo = tabela.convertRowIndexToModel(linhaTela);
        return modelo.getPedido(linhaModelo);
    }

    public void selecionarPorId(int id) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getPedido(i).getIdPedido() == id) {
                int linhaTela = tabela.convertRowIndexToView(i);
                tabela.setRowSelectionInterval(linhaTela, linhaTela);
                tabela.scrollRectToVisible(tabela.getCellRect(linhaTela, 0, true));
                return;
            }
        }
        tabela.clearSelection();
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
        cbFiltroStatus.setEnabled(!editando);
    }

    public void setCarregando(boolean carregando) {
        tabela.setEnabled(!carregando);
        btnNovo.setEnabled(!carregando);
        setCursor(carregando ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
        if (carregando) mostrarStatus("Carregando...");
    }

    public void mostrarStatus(String texto) { lblBarra.setText(" " + texto); }
    public void mostrarMensagem(String texto) { JOptionPane.showMessageDialog(this, texto); }
    public void mostrarErro(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
    public boolean confirmar(String pergunta) {
        return JOptionPane.showConfirmDialog(this, pergunta, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    public void limparSelecao() { tabela.clearSelection(); }
    public void fechar() { dispose(); }

    public String getBusca() { return txtBusca.getText().trim(); }

    /** null significa "(todos)". */
    public Status getStatusSelecionado() {
        Object sel = cbFiltroStatus.getSelectedItem();
        if (sel == null || "(todos)".equals(sel)) return null;
        return Status.valueOf(sel.toString());
    }

    // ---- Grupo 1: getters usados pelo Controller ----
    public JButton getBtnNovo() { return btnNovo; }
    public JButton getBtnEditar() { return btnEditar; }
    public JButton getBtnExcluir() { return btnExcluir; }
    public JButton getBtnSalvar() { return btnSalvar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public JComboBox<Produto> getCbProduto() { return cbProduto; }
    public JButton getBtnAdicionar() { return btnAdicionar; }
    public JButton getBtnRemoverItem() { return btnRemoverItem; }
    public JTextField getTxtBusca() { return txtBusca; }
    public JComboBox<String> getCbFiltroStatus() { return cbFiltroStatus; }
    public JTable getTabela() { return tabela; }
}