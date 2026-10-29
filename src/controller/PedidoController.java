package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.PedidoRepositorioBD;
import model.Pedido;
import model.Produto;
import view.JanelaPedido;

/**
 * CONTROLLER - liga a tela (View) ao repositorio (Model).
 * Regra de ouro: todo evento que muda dados termina em atualizarTela().
 */
public class PedidoController implements ActionListener, ListSelectionListener {
    private final JanelaPedido viewp;
    private final PedidoRepositorioBD repositoriop;

    private boolean recarregando = false;   // true enquanto a tela esta sendo repovoada
    private boolean editando = false;       // true enquanto o formulario esta aberto
    private Pedido emEdicao = null;         // pedido original (null = pedido novo)
    private final Timer esperaBusca;        // espera o usuario parar de digitar

    public PedidoController(PedidoRepositorioBD repositoriop, JanelaPedido viewp) {
        this.viewp = viewp;
        this.repositoriop = repositoriop;

        this.viewp.getBtnNovo().addActionListener(this);
        this.viewp.getBtnEditar().addActionListener(this);
        this.viewp.getBtnExcluir().addActionListener(this);
        this.viewp.getBtnSalvar().addActionListener(this);
        this.viewp.getBtnCancelar().addActionListener(this);
        this.viewp.getCbFiltroStatus().addActionListener(this);
        this.viewp.getBtnAdicionar().addActionListener(this);
        this.viewp.getBtnRemoverItem().addActionListener(this);
        this.viewp.getTabela().getSelectionModel().addListSelectionListener(this);

        // busca "ao digitar": cada tecla reinicia o relogio
        this.esperaBusca = new Timer(300, e -> atualizarTela(idSelecionado()));
        this.esperaBusca.setRepeats(false);
        this.viewp.getTxtBusca().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { buscaMudou(); }
            public void removeUpdate(DocumentEvent e) { buscaMudou(); }
            public void changedUpdate(DocumentEvent e) { buscaMudou(); }
        });

        atualizarTela(0);
    }

    public void iniciarTela() { this.viewp.setVisible(true); }

    private void buscaMudou() {
        if (!recarregando) esperaBusca.restart();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || recarregando || editando) return;
        atualizarDetalhe();
    }

    private void atualizarDetalhe() {
        Pedido ped = this.viewp.getPedidoSelecionado();
        if (ped == null) this.viewp.mostrarVazio();
        else             this.viewp.mostrarDetalhe(ped);
        this.viewp.atualizarBotoes(ped != null, this.editando);
    }

    private int idSelecionado() {
        Pedido ped = this.viewp.getPedidoSelecionado();
        return (ped == null) ? 0 : ped.getIdPedido();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (recarregando) return;

        Object origem = e.getSource();
        if (origem == this.viewp.getBtnNovo()) {
            this.viewp.limparSelecao();
            entrarEdicao(null);
        } else if (origem == this.viewp.getBtnEditar()) {
            entrarEdicao(this.viewp.getPedidoSelecionado());
        } else if (origem == this.viewp.getBtnExcluir()) {
            excluir();
        } else if (origem == this.viewp.getBtnSalvar()) {
            salvar();
        } else if (origem == this.viewp.getBtnCancelar()) {
            this.editando = false;
            this.emEdicao = null;
            atualizarDetalhe();
        } else if (origem == this.viewp.getCbFiltroStatus()) {
            atualizarTela(idSelecionado());
        } else if (origem == this.viewp.getBtnAdicionar()) {
            adicionarItem();
        } else if (origem == this.viewp.getBtnRemoverItem()) {
            removerItem();
        }
    }

    private void adicionarItem() {
        Produto prod = this.viewp.getProdutoSelecionado();
        if (prod == null) {
            this.viewp.mostrarErro("Escolha um produto no combo.");
            return;
        }
        if (!this.viewp.adicionarItem(prod)) {
            this.viewp.mostrarErro("Esse produto já está no pedido.");
            return;
        }
        this.viewp.setPrecoTotal(this.viewp.somarItens()); // total = soma dos itens (ainda editavel)
    }

    private void removerItem() {
        if (!this.viewp.removerItemSelecionado()) {
            this.viewp.mostrarErro("Selecione um item da lista para remover.");
            return;
        }
        this.viewp.setPrecoTotal(this.viewp.somarItens());
    }

    private void entrarEdicao(Pedido ped) {
        this.editando = true;
        this.emEdicao = ped;
        // recarrega os produtos (podem ter sido cadastrados na outra tela).
        // recarregando=true evita que o combo dispare o preenchimento do preco.
        this.recarregando = true;
        try {
            this.viewp.carregarProdutos(this.repositoriop.listarProdutos());
            this.viewp.editarPedido(ped);
        } finally {
            this.recarregando = false;
        }
        this.viewp.atualizarBotoes(ped != null, true);
    }

    private void salvar() {
        Pedido ped;
        try {
            // CONVERSAO: JTextField sempre devolve texto; a fronteira com a tela e quem converte.
            ped = this.viewp.lerFormulario();
        } catch (NumberFormatException ex) {
            this.viewp.mostrarErro("O preço total deve ser um número válido. Ex.: 12,50");
            return;
        }
        // Os produtos vem da lista de itens do formulario.
        ArrayList<Produto> itens = this.viewp.getItens();
        if (itens.isEmpty()) {
            this.viewp.mostrarErro("Adicione pelo menos um produto ao pedido.");
            return;
        }
        ped.setProdutos(itens);

        try {
            if (ped.getIdPedido() == 0) this.repositoriop.salvar(ped);
            else                        this.repositoriop.alterar(ped);
        } catch (IllegalArgumentException ex) {
            this.viewp.mostrarErro(ex.getMessage());
            return;
        }
        this.editando = false;
        this.emEdicao = null;
        atualizarTela(ped.getIdPedido());
        this.viewp.mostrarStatus("Salvo: pedido " + ped.getIdPedido());
    }

    private void excluir() {
        Pedido ped = this.viewp.getPedidoSelecionado();
        if (ped != null && this.viewp.confirmar("Excluir o pedido " + ped.getIdPedido() + "?")) {
            try {
                this.repositoriop.excluir(ped.getIdPedido());
            } catch (IllegalArgumentException ex) {
                this.viewp.mostrarErro(ex.getMessage());
                return;
            }
            atualizarTela(0);
        }
    }

    /** O UNICO metodo que repovoa a tela. */
    private void atualizarTela(int idParaSelecionar) {
        this.recarregando = true;
        try {
            List<Pedido> lista = this.repositoriop.listarFiltrado(
                    this.viewp.getStatusSelecionado(), this.viewp.getBusca());
            this.viewp.carregarTabela(lista);
            this.viewp.carregarResumo(this.repositoriop.contarPorStatus());
            this.viewp.selecionarPorId(idParaSelecionar);
            this.viewp.mostrarStatus(lista.size() + " registro(s) em exibicao.");
        } finally {
            this.recarregando = false;
        }
        atualizarDetalhe();
    }
}