
package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Pedido {
	
	/**
	 * MODEL - a entidade do sistema.
	 *
	 * PASSO 3 do roteiro. Note o padrao:
	 * - um atributo private por campo da ficha;
	 * - um get e um set por atributo;
	 * - um metodo validar() com as regras que valem para UM animal sozinho.
	 *
	 * Nao ha nenhum import. Esta classe nao sabe que existe tela nem banco.
	 */

	
    private ArrayList<Produto> produtos;
    private int idPedido;
    private double precoTotal;
    private Status status_pedido;
    private Pagamento tipo_pagamento;
    private LocalDateTime horario;
    

    public Pedido(double precoTotal, Status status_pedido, Pagamento tipo_pagamento) {
        this.produtos = new ArrayList<Produto>();
        this.precoTotal = precoTotal;
        this.status_pedido = status_pedido;
        this.tipo_pagamento = tipo_pagamento;
        this.horario = LocalDateTime.now();
    }

    public Pedido() {
		// TODO Auto-generated constructor stub
	}

	/**
     * AS REGRAS DESTA ENTIDADE, sozinha.
     *
     * Devolve null quando esta tudo certo, ou o texto do problema.
     * Assim o Controller so precisa testar se veio null, e a mensagem
     * ja vem pronta para ser exibida.
     *
     * O que NAO cabe aqui: "ja existe um animal com este nome para este
     * dono?". Isso depende dos OUTROS animais, e um objeto sozinho nao
     * conhece os outros. Essa regra vai para o repositorio.
     */

    public String validar() {

        if (produtos == null || produtos.isEmpty()) {
            return "Adicione pelo menos um produto ao pedido.";
        }

        if (precoTotal <= 0) {
            return "O preço total deve ser maior que zero.";
        }

        if (tipo_pagamento == null) {
            return "Selecione o tipo de pagamento.";
        }

        if (status_pedido == null) {
            return "Selecione o status do pedido.";
        }

        return null;
    }

    public ArrayList<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(ArrayList<Produto> produtos) {
        this.produtos = produtos;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Status getStatus_pedido() {
        return status_pedido;
    }

    public void setStatus_pedido(Status status_pedido) {
        this.status_pedido = status_pedido;
    }

    public Pagamento getTipo_pagamento() {
        return tipo_pagamento;
    }

    public void setTipo_pagamento(Pagamento tipo_pagamento) {
        this.tipo_pagamento = tipo_pagamento;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
    
}
