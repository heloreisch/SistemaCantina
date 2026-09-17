package model;

import java.time.LocalTime;

public class Produto {

			private String nome;
			private double preco;
			private Categoria categoria;
			
			
			/* REGRAS
			 *  - NOME: não pode conter caracteres especiais
			 *  - CATEGORIA: deve estar disponível no horário específico - tratamento feito na hora do pedido
			 *  - PRECO: preço maior que zero
			 */
			public String getNome() {
				return nome;
			}
			public void setNome(String nome) {
				this.nome = nome;
			}
			public double getPreco() {
				return preco;
			}
			public void setPreco(double preco) {
				this.preco = preco;
			}
			public Categoria getCategoria() {
				return categoria;
			}
			public void setCategoria(Categoria categoria) {
				this.categoria = categoria;
			}
			
			public String validar() {
				if(nome == null || nome.trim().isEmpty()) {
					return "Preencha o nome";
				}
				
				if(preco<0) {
					return "O preço deve ser maior que zero";
				}
				
				if(categoria == null) {
					return "Indique uma categoria!";
				}
				return null;
//					ADICIONAR EM PEDIDO
//				LocalTime horario = dataHora.toLocalTime();
//				if (!(p.getHorario().isAfter(LocalTime.of(11, 0)) && p.getHorario().isBefore(LocalTime.of(13, 0)))&&categoria.MARMITA) {
//				    return "Essa categoria não está disponível para este horário.";
//				}
//				
//				if (!(p.getHorario().isAfter(LocalTime.of(7, 0)) && p.getHorario().isBefore(LocalTime.of(22, 0)))) {
//				    return "A catina está fechada nesse horário.";
//				}
				
			}
			
		
}
