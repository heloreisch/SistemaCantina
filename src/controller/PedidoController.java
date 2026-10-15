
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Pedido;
import model.PedidoRepositorioBD;
import model.Status;
import model.Pagamento;
import view.JanelaPedido;

/**
 * CONTROLLER - PASSO 8 do roteiro.
 *
 * Um metodo por botao, e um actionPerformed que descobre qual foi clicado.
 */

public class PedidoController implements ActionListener {

    private PedidoRepositorioBD repositoriop;
    private JanelaPedido viewp;

    public PedidoController(PedidoRepositorioBD repositoriop,JanelaPedido viewp) {

        this.repositoriop = repositoriop;
        this.viewp = viewp;

     // Um addActionListener por botao. O "this" e o proprio Controller
     // se regi
        this.viewp.getBtnCadastrar().addActionListener(this);
        this.viewp.getBtnLimpar().addActionListener(this);
        this.viewp.getBtnFechar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	// == e nao equals: a pergunta e "e exatamente aquele objeto?".
        if (e.getSource() == this.viewp.getBtnCadastrar()) {
            cadastrar();

        } else if (e.getSource() == this.viewp.getBtnLimpar()) {
            this.viewp.limparCampos();

        } else if (e.getSource() == this.viewp.getBtnFechar()) {
            this.viewp.fechar();
        }
    }

    private void cadastrar() {
    	// CONVERSAO: tudo que vem de um JTextField e TEXTO, mesmo que
    	 // pareca numero. Converter e trabalho de quem faz fronteira com a
    	 // tela - por isso este try/catch fica aqui, e nao no Model.
    	 // O replace resolve o detalhe brasileiro: digitamos 12,5 e o Java
    	 // so entende 12

        Pedido pedido = new Pedido();

        // Converter o texto do preço para double
        double precoTotal;

        try {
            precoTotal = Double.parseDouble(
                this.viewp.getTxtPrecoTotal().getText().trim().replace(",", ".")
            );

        } catch (NumberFormatException erro) {
            this.viewp.mostrarErro( "O preço total deve ser um número válido.");
            return;
     }
        pedido.setPrecoTotal(precoTotal);
        pedido.setStatus_pedido(
            (Status) this.viewp.getCbStatus().getSelectedItem()
        );

        pedido.setTipo_pagamento((Pagamento) this.viewp.getCbPagamento().getSelectedItem()
        );

        try {
            this.repositoriop.salvar(pedido);

        } catch (IllegalArgumentException erro) {
            this.viewp.mostrarErro(erro.getMessage());
            return;
        }

        this.viewp.mostrarMensagem( "Pedido cadastrado! Total de pedidos: " + this.repositoriop.contar());
        this.viewp.limparCampos();
    }

    public void iniciarTela() {
        this.viewp.setVisible(true);
    }
}
