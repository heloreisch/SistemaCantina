package view;

import model.Produto;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
/**
 * O modelo da tabela: liga a List<Animal> ao JTable.
 *
 * Com DefaultTableModel a tabela guarda TEXTOS; a linha selecionada
 * devolve celulas, e para saber quem foi selecionado e preciso ler a
 * celula e converter de volta. Com um modelo proprio, getAnimal(linha)
 * devolve o OBJETO inteiro.
 */

public class ProdutoTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	 private static final String[] COLUNAS = { "Nome", "Categoria", "Preço (R$)"};
	 // Atributo, e nao variavel local: o JTable consulta o modelo a cada
	 // repintura da tela, e precisa encontrar os dados la.
	 private List<Produto> dados = new ArrayList<>();
	 @Override
	 public int getRowCount() {
	 return dados.size();
	 }
	 @Override
	 public int getColumnCount() {
	 return COLUNAS.length;
	 }
	 @Override
	 public Object getValueAt(int linha, int coluna) {
	 Produto a = dados.get(linha);
	 switch (coluna) {
	 case 0: return a.getNome();
	 case 1: return a.getCategoria();
	 case 2: return a.getPreco();
	 default: return "";
	 }
	 }
	 @Override
	 public String getColumnName(int coluna) {
	 return COLUNAS[coluna];
	 }
	 /**
	 * Sem este metodo o JTable trata tudo como texto: a ordenacao por
	 * peso sairia 12,5 antes de 4,2 (ordem alfabetica), e os numeros
	 * ficariam alinhados a esquerda.
	 */
	 @Override
	 public Class<?> getColumnClass(int coluna) {
	 return (coluna == 2) ? Double.class : String.class;
	 }
	 @Override
	 public boolean isCellEditable(int linha, int coluna) {
	 return false; // alterar passa pelo formulario, nao pela celula
	 }
	 /**
	  * Troca o conteudo inteiro. O fireTableDataChanged avisa o JTable de
	  * que ele precisa se redesenhar. SEM essa chamada, a lista muda em
	  * memoria e a tela continua mostrando os dados antigos.
	  */
	  public void setDados(List<Produto> novos) {
	  this.dados = novos;
	  fireTableDataChanged();
	  }
	  /** Devolve o objeto completo da linha, e nao textos de celula. */
	  public Produto getProduto(int linha) {
	  return dados.get(linha);
	  }
	 }

