package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

import model.Categoria;

/**
 * VIEW - a tela. PASSO 7 do roteiro.
 *
 * Um par rotulo + campo por atributo do Model, e os botoes.
 *
 * Dois grupos de metodos, e nada alem disso:
 * Grupo 1 - get... devolvendo componentes ao Controller;
 * Grupo 2 - metodos que exibem ou fazem o que mandarem.
 *
 * Confira com Ctrl+F: nao ha "addActionListener" e nao ha "if" aqui.
 */
public class JanelaProduto extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    // Um atributo por campo da tela.
    private JTextField txtNome;
    private JTextField txtPreco;
    private JComboBox<Categoria> comboCategoria;

    // Os botoes tambem sao atributos: o Controller precisa alcanca-los
    // para pendurar o ouvinte.
    private JButton btnCadastrar;
    private JButton btnLimpar;
    private JButton btnFechar;

    public JanelaProduto() {

        setTitle("Cadastro de Produtos - Cantina");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 460, 300);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(10, 21, 80, 14);
        contentPane.add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(110, 18, 110, 20);
        contentPane.add(txtNome);

        JLabel lblEspecie = new JLabel("Categoria");
        lblEspecie.setBounds(10, 60, 80, 14);
        contentPane.add(lblEspecie);

        comboCategoria = new JComboBox<>(Categoria.values());
        comboCategoria.setBounds(110, 57, 110, 20);
        contentPane.add(comboCategoria);

        JLabel lblPeso = new JLabel("Preço (R$)");
        lblPeso.setBounds(10, 99, 80, 14);
        contentPane.add(lblPeso);

        txtPreco = new JTextField();
        txtPreco.setBounds(110, 96, 110, 20);
        contentPane.add(txtPreco);

        // 105 px: com menos, o texto "Cadastrar" sai cortado.
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(250, 17, 105, 22);
        contentPane.add(btnCadastrar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(250, 56, 105, 22);
        contentPane.add(btnLimpar);

        btnFechar = new JButton("Fechar");
        btnFechar.setBounds(250, 95, 105, 22);
        contentPane.add(btnFechar);
    }

    // ---- Grupo 1: entregar componentes ao Controller ------------------

    public JTextField getTxtNome() {
        return txtNome;
    }

    public Categoria getCategoria() {
        return (Categoria)comboCategoria.getSelectedItem();
    }

    public JTextField getTxtPreco() {
        return txtPreco;
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

    // ---- Grupo 2: capacidades da propria tela -------------------------

    public void mostrarMensagem(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    public void mostrarErro(String texto) {
        JOptionPane.showMessageDialog(
            this,
            texto,
            "Atencao",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public void limparCampos() {
        txtNome.setText("");
        comboCategoria.setSelectedIndex(0);
        txtPreco.setText("");
        txtNome.requestFocus();
    }

    public void fechar() {
        dispose();
    }
}