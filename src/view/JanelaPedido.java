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

    public void fechar() {
        dispose();
    }
}