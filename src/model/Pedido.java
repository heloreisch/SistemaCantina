package model;

public class Pedido {
	
	private Produto produto; /*fazer um array */
	 /*id, precoTotal, horario, status_pedido, tipo_pagamento */
	
	private double precoTotal;
	private Status status_pedido;
	private Pagamento tipo_pagamento;
	//private LocalDateTime horario;
	
	
	public Pedido(Produto produto, double precoTotal, Status status_pedido, Pagamento tipo_pagamento) {
		super();
		this.produto = produto;
		this.precoTotal = precoTotal;
		this.status_pedido = status_pedido;
		this.tipo_pagamento = tipo_pagamento;
	}
		
			Produto p = new Produto();
	
	//  AS REGRAS PEDIDO
	
	public String validar() {
	/*	 if (produto == null || produto.trim().isEmpty()) {
		 return "Preencha o nome do animal.";
		 } */
		 if (precoTotal == 0  || precoTotal < 0) {
			 return "Pedido inválido" ;
					 
		 }
		 if( tipo_pagamento == null ) {
			 return "Pedido inválido" ;
			 
		 }
		 if(produto.isEmpty() || produto == null) {
			 return "Pedido inválido";
		 }
	 return "Pedido válido";
		}
		
	
	
	
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
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
	
	
	
	
}
