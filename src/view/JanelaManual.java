package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

/**
 * VIEW - janela do manual de uso.
 *
 * O texto do manual NAO esta aqui: esta no arquivo manual/manual.html,
 * dentro do projeto. Para corrigir o manual, edita-se o HTML.
 */
public class JanelaManual extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO = "/util/manual.html";

    public JanelaManual(JFrame dono) {
        super(dono, "Manual de uso", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 460);
        setMinimumSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(dono);

        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // ---------- CENTER: o manual, com barra de rolagem ----------
        JEditorPane editor = new JEditorPane();
        editor.setEditable(false);                    // so leitura
        carregar(editor);
        contentPane.add(new JScrollPane(editor), BorderLayout.CENTER);

        // ---------- SOUTH: botao ----------
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotao.add(btnFechar);
        contentPane.add(painelBotao, BorderLayout.SOUTH);

        getRootPane().registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /** Carrega o HTML de dentro do projeto; se falhar, mostra um aviso. */
    private void carregar(JEditorPane editor) {
        URL url = JanelaManual.class.getResource(ARQUIVO);
        if (url == null) {
            editor.setContentType("text/plain");
            editor.setText("Manual nao encontrado: " + ARQUIVO);
            return;
        }
        try {
            editor.setPage(url);   // detecta que e HTML pela extensao
        } catch (IOException erro) {
            editor.setContentType("text/plain");
            editor.setText("Nao foi possivel abrir o manual: " + erro.getMessage());
        }
    }
}