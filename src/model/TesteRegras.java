package model;

public class TesteRegras {
	public static void main(String[] args) {
		
	 Produto p = new Produto();
	 p.setNome("Coxinha");
	 p.setPreco(8);
	 p.setCategoria(Categoria.SALGADO);
	 System.out.println("Coxinha -> " + p.validar());
	 
}
}
