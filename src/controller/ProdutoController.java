package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import model.Categoria;
import model.Produto;
import model.ProdutoRepositorioBD;
import view.JanelaProduto;
/**
 * CONTROLLER - PASSO 8 do roteiro.
 *
 * Um metodo por botao, e um actionPerformed que descobre qual foi clicado.
 */
public class ProdutoController implements ActionListener {
	//final não muda
	private final JanelaProduto view;
	private final ProdutoRepositorioBD repositorio;
	
	public ProdutoController(ProdutoRepositorioBD repositorio, JanelaProduto view) {
		
		this.repositorio = repositorio;
		this.view = view;
		
		List<Categoria> categorias = repositorio.listarCategorias();
		this.view.carregarCadastro(categorias);
		this.view.carregarFiltro(categorias);
		
		// Um addActionListener por botao. O "this" e o proprio Controller
		// se registrando como ouvinte.
		this.view.getBtnIrCadastro().addActionListener(this);
		this.view.getBtnIrLista().addActionListener(this);
		this.view.getBtnFiltrar().addActionListener(this);
		this.view.getBtnSalvar().addActionListener(this);
		// Não registre ouvinte nos combos.
		
		
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// == e nao equals: a pergunta e "e exatamente aquele objeto?".
		Object src = e.getSource();
		if (src == view.getBtnIrCadastro()) {
			view.mostrarCartao("cadastro");
		} else if (src == view.getBtnIrLista()) {
			view.mostrarCartao("lista");
			filtrar();
		} else if (src == view.getBtnFiltrar()) {
			filtrar();
		} else if (src == view.getBtnSalvar()) {
			salvar();
		}
	}
	
	private void filtrar() {
		Categoria c = view.getCategoriaFiltro();
		List<Produto> lista = (c == null)
				? repositorio.listarTodos()
				: repositorio.listarPorCategoria(c);
		view.carregarTabela(lista);
		view.mostrarStatus(lista.size() + " produto(s).");
	}

	private void salvar() {
		double preco;
		try {
			preco = Double.parseDouble(view.getTxtPreco().getText().trim().replace(",", "."));
		} catch (NumberFormatException erro) {
			view.mostrarErro("O preço deve ser um número.");
			return;
		}
		Produto p = new Produto();
		p.setNome(view.getTxtNome().getText());
		p.setCategoria(view.getCategoriaCadastro());
		p.setPreco(preco);
		try {
			repositorio.selecionar(p);
		} catch (IllegalArgumentException erro) {
			view.mostrarErro(erro.getMessage());
			return;
		}
		view.mostrarMensagem("Cadastrado. Total: " + repositorio.contar());
		view.limparCampos();
	}

	public void iniciarTela() {
		view.setVisible(true);
	}
	
}
