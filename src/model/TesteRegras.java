package model;

public class TesteRegras {
	public static void main(String[] args) {
		
	 Produto p = new Produto();
	 p.setNome("Coxinha");
	 p.setPreco(8);
	 p.setCategoria(Categoria.SALGADO);
	 System.out.println("Coxinha -> " + p.validar());
	 
	 Pedido ped = new Pedido (p, 10);
	 ped.setPrecoTotal(10);
	 ped.setProduto(p);
	 ped.setTipo_pagamento(Pagamento.CARTÃO);
	 ped.setStatus_pedido(Status.PREPARANDO);
	 System.out.println("Pedido -> " + ped.validar());
	 
	 Produto b = new Produto();
	 p.setNome("Pão de queijo");
	 p.setPreco(-1);
	 p.setCategoria(Categoria.SALGADO);
	 System.out.println("Coxinha -> " + p.validar());
	 
}
}
