package view;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import util.InfoApp;
/**
* VIEW - janela "Sobre": quem fez e do que se trata o software.
*
* E um JDialog: uma janela secundaria, presa a janela principal (o "dono").
* Modal = enquanto estiver aberta, a janela principal nao recebe cliques.
*/
		public class JanelaSobre extends JDialog {
			private static final long serialVersionUID = 1L;
			public JanelaSobre(JFrame dono) {
				super(dono, "Sobre " + InfoApp.NOME, true); // true = modal
				setDefaultCloseOperation(DISPOSE_ON_CLOSE); // fechar so esta janela
				setSize(420, 360);
				setMinimumSize(getSize());
				setLocationRelativeTo(dono); // centralizada sobre o dono
				JPanel contentPane = new JPanel(new BorderLayout(0, 10));
				contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
				setContentPane(contentPane);
				
		// ---------- CENTER: o texto ----------
				JLabel lblTexto = new JLabel(montarTexto(), SwingConstants.CENTER);
				contentPane.add(lblTexto, BorderLayout.CENTER);
		// ---------- SOUTH: botao ----------
				JButton btnFechar = new JButton("Fechar");
				btnFechar.addActionListener(e -> dispose());
				JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
				painelBotao.add(btnFechar);
				contentPane.add(painelBotao, BorderLayout.SOUTH);
				getRootPane().setDefaultButton(btnFechar); // Enter fecha
		// ESC tambem fecha
				getRootPane().registerKeyboardAction(e -> dispose(),
						KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
						JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
			/** Monta o texto em HTML simples (o JLabel entende <b>, <br>, <h2>...). */
			private String montarTexto() {
				StringBuilder sb = new StringBuilder("<html><div style='text-align:center'>");
				sb.append("<h2>").append(InfoApp.NOME).append("</h2>");
				sb.append("Versao ").append(InfoApp.VERSAO).append("<br><br>");
				sb.append(InfoApp.DESCRICAO).append("<br><br>");
				sb.append("<b>Desenvolvido por</b><br>");
				
				for (String autor : InfoApp.AUTORES) {
					sb.append(autor).append("<br>");
					}
					sb.append("<br><b>Professor</b><br>").append(InfoApp.PROFESSOR).append("<br><br>");
					sb.append(InfoApp.INSTITUICAO).append("<br>");
					sb.append(InfoApp.DISCIPLINA).append(" - ").append(InfoApp.ANO).append("<br><br>");
					sb.append("<small>").append(InfoApp.TECNOLOGIAS).append("</small>");
					sb.append("</div></html>");
					return sb.toString();
					}
				}

