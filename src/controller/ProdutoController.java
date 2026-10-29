package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Categoria;
import model.Produto;
import model.ProdutoRepositorioBD;
import view.JanelaProduto;
/**
 * CONTROLLER - PASSO 8 do roteiro.
 *
 * Um metodo por botao, e um actionPerformed que descobre qual foi clicado.
 */
public class ProdutoController implements ActionListener, ListSelectionListener {
    private final JanelaProduto view;
    private final ProdutoRepositorioBD bd;

    private boolean recarregando = false;   // true enquanto a tela esta sendo repovoada
    private boolean editando = false;       // true enquanto o formulario esta aberto
    private final Timer esperaBusca;        // espera o usuario parar de digitar

    public ProdutoController(JanelaProduto view, ProdutoRepositorioBD bd) {
        this.view = view;
        this.bd = bd;

        // registro dos listeners
        this.view.getBtnNovo().addActionListener(this);
        this.view.getBtnEditar().addActionListener(this);
        this.view.getBtnExcluir().addActionListener(this);
        this.view.getBtnSalvar().addActionListener(this);
        this.view.getBtnCancelar().addActionListener(this);
        this.view.getCbEspecie().addActionListener(this);
        this.view.getTabela().getSelectionModel().addListSelectionListener(this);

        // busca "ao digitar": cada tecla reinicia o relogio
        this.esperaBusca = new Timer(300, e -> atualizarTela(idSelecionado()));
        this.esperaBusca.setRepeats(false);
        this.view.getTxtBusca().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscaMudou(); }
            public void removeUpdate(DocumentEvent e) { buscaMudou(); }
            public void changedUpdate(DocumentEvent e) { buscaMudou(); }
        });

        atualizarTela(0);
    }

    private void buscaMudou() {
        if (!recarregando) esperaBusca.restart();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || recarregando || editando) return;
        atualizarDetalhe();
    }

    private void atualizarDetalhe() {
        Produto a = this.view.getProdutoSelecionado();
        if (a == null) this.view.mostrarVazio();
        else           this.view.mostrarDetalhe(a);
        this.view.atualizarBotoes(a != null, this.editando);
    }

    private int idSelecionado() {
        Produto a = this.view.getProdutoSelecionado();
        return (a == null) ? 0 : a.getId();
    }
    
    public void actionPerformed(ActionEvent e) {
        if (recarregando) return; // o combo dispara evento enquanto e preenchido

        Object origem = e.getSource();
        if (origem == this.view.getBtnNovo()) {
            this.view.limparSelecao();
            entrarEdicao(null);
        } else if (origem == this.view.getBtnEditar()) {
            entrarEdicao(this.view.getProdutoSelecionado());
        } else if (origem == this.view.getBtnExcluir()) {
            excluir();
        } else if (origem == this.view.getBtnSalvar()) {
            salvar();
        } else if (origem == this.view.getBtnCancelar()) {
            this.editando = false;
            atualizarDetalhe();
        } else if (origem == this.view.getCbCategoria()) {
            atualizarTela(idSelecionado());
        }
    }

    private void entrarEdicao(Produto a) {
        this.editando = true;
        this.view.editarProduto(a);
        this.view.atualizarBotoes(a != null, true);
    }

    private void salvar() {
        try {
            Produto a = this.view.lerFormulario();
            if (a.getNome().isEmpty()) {
                this.view.mostrarStatus("Informe o nome.");
                return;
            }
            this.bd.salvar(a);
            this.editando = false;
            atualizarTela(a.getId());
            this.view.mostrarStatus("Salvo: " + a.getNome());
        } catch (NumberFormatException ex) {
            this.view.mostrarStatus("Peso invalido. Use numeros, ex.: 4.5");
        }
    }

    private void excluir() {
        Produto a = this.view.getProdutoSelecionado();
        if (a != null && this.view.confirmar("Excluir " + a.getNome() + "?")) {
            this.bd.excluir(a.getId());
            atualizarTela(0);
        }
    }
    /** O UNICO metodo que repovoa a tela. */
    private void atualizarTela(int idParaSelecionar) {
        this.recarregando = true;
        try {
            String especie = this.view.getProdutoSelecionado();
            this.view.carregarCategorias(this.bd.listarCategorias(), especie);

            List<Produto> lista = this.bd.listarFiltrado(
                    this.view.getProdutoSelecionado(), this.view.getBusca());
            this.view.carregarTabela(lista);
            this.view.carregarResumo(this.bd.contarPorCategoria());
            this.view.selecionarPorId(idParaSelecionar);
            this.view.mostrarStatus(lista.size() + " registro(s) em exibicao.");
        } finally {
            this.recarregando = false;
        }
        atualizarDetalhe();
    }

    // (opcional) mantenha aqui o atualizarTelaAssincrono se quiser exemplo de SwingWorker
}
    
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		// == e nao equals: a pergunta e "e exatamente aquele objeto?".
//		Object src = e.getSource();
//		if (src == view.getBtnIrCadastro()) {
//			view.mostrarCartao("cadastro");
//		} else if (src == view.getBtnIrLista()) {
//			view.mostrarCartao("lista");
//			filtrar();
//		} else if (src == view.getBtnFiltrar()) {
//			filtrar();
//		} else if (src == view.getBtnSalvar()) {
//			salvar();
//		}
//	}
//	
//	private void filtrar() {
//		Categoria c = view.getCategoriaFiltro();
//		List<Produto> lista = (c == null)
//				? repositorio.listarTodos()
//				: repositorio.listarPorCategoria(c);
//		view.carregarTabela(lista);
//		view.mostrarStatus(lista.size() + " produto(s).");
//	}
//
//	private void salvar() {
//		double preco;
//		try {
//			preco = Double.parseDouble(view.getTxtPreco().getText().trim().replace(",", "."));
//		} catch (NumberFormatException erro) {
//			view.mostrarErro("O preço deve ser um número.");
//			return;
//		}
//		Produto p = new Produto();
//		p.setNome(view.getTxtNome().getText());
//		p.setCategoria(view.getCategoriaCadastro());
//		p.setPreco(preco);
//		try {
//			repositorio.selecionar(p);
//		} catch (IllegalArgumentException erro) {
//			view.mostrarErro(erro.getMessage());
//			return;
//		}
//		view.mostrarMensagem("Cadastrado. Total: " + repositorio.contar());
//		view.limparCampos();
//	}
//
//	public void iniciarTela() {
//		view.setVisible(true);
//	}
//	
//}
