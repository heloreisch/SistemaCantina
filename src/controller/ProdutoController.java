package controller;

import model.Produto;
import model.ProdutoRepositorioBD;
import view.JanelaProduto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CONTROLLER - PASSO 8 do roteiro.
 *
 * Um metodo por botao, e um actionPerformed que descobre qual foi clicado.
 */
public class ProdutoController implements ActionListener {
	private JanelaProduto view;
	ProdutoRepositorioBD repositorio = new ProdutoRepositorioBD();
	janela.carregarCategorias(
		    repositorio.listarCategorias()
		);
	
	public ProdutoController(ProdutoRepositorioBD repositorio, JanelaProduto view) {
		this.repositorio = repositorio;
		this.view = view;
		// Um addActionListener por botao. O "this" e o proprio Controller
		// se registrando como ouvinte.
		this.view.getBtnIrCadastro().addActionListener(this);
		this.view.getBtnIrLista().addActionListener(this);
		this.view.getBtnFiltrar().addActionListener(this);
		this.view.getBtnSalvar().addActionListener(this);
		this.view.getCategoria().addActionListener(this);
		this.view.getTabela();
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// == e nao equals: a pergunta e "e exatamente aquele objeto?".
		if (e.getSource() == this.view.getBtnCadastrar()) {
			cadastrar();
		} else if (e.getSource() == this.view.getBtnLimpar()) {
			this.view.limparCampos();
		} else if (e.getSource() == this.view.getBtnFechar()) {
			this.view.fechar();
		}
	}

	private void cadastrar() {
		// CONVERSAO: tudo que vem de um JTextField e TEXTO, mesmo que
		// pareca numero. Converter e trabalho de quem faz fronteira com a
		// tela - por isso este try/catch fica aqui, e nao no Model.
		// O replace resolve o detalhe brasileiro: digitamos 12,5 e o Java
		// so entende 12.5.
		double preco;
		try {
			preco = Double.parseDouble(this.view.getTxtPreco().getText().trim().replace(",", "."));
		} catch (NumberFormatException erro) {
			this.view.mostrarErro("O preço deve ser um numero.");
			return;
		}
//Objeto NOVO a cada cadastro: reaproveitar um unico faria o
// segundo sobrescrever o primeiro.
		Produto produto = new Produto();
		produto.setNome(this.view.getTxtNome().getText());
		produto.setCategoria(this.view.getCategoria());
		produto.setPreco(preco);
		try {
			this.repositorio.selecionar(produto);
		} catch (IllegalArgumentException erro) {
			this.view.mostrarErro(erro.getMessage());
			return; // sem o return, a mensagem de sucesso viria depois
		}
		this.view.mostrarMensagem("Cadastrado. Total: " + this.repositorio.contar());
		this.view.limparCampos();
	}

	public void iniciarTela() {
		this.view.setVisible(true);
	}
}
