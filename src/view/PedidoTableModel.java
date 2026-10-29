package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Pedido;
import model.Produto;

/** Modelo da JTable: guarda a lista e diz a tabela o que desenhar. */
public class PedidoTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = { "ID", "Produtos", "Preço total", "Status", "Pagamento" };
    private List<Pedido> dados = new ArrayList<>();

    public void setDados(List<Pedido> lista) {
        this.dados = new ArrayList<>(lista);
        fireTableDataChanged();
    }

    /** "Coxinha, Suco" - usa so o nome, sem depender do toString() do Produto. */
    public static String nomesProdutos(List<Produto> lista) {
        StringBuilder sb = new StringBuilder();
        for (Produto p : lista) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(p.getNome());
        }
        return sb.toString();
    }

    public Pedido getPedido(int linha) {
        return dados.get(linha);
    }

    @Override public int getRowCount() { return dados.size(); }
    @Override public int getColumnCount() { return COLUNAS.length; }
    @Override public String getColumnName(int c) { return COLUNAS[c]; }

    @Override
    public Class<?> getColumnClass(int c) {
        switch (c) {
            case 0: return Integer.class;   // ordena como numero
            case 2: return Double.class;
            default: return String.class;
        }
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Pedido p = dados.get(linha);
        switch (coluna) {
            case 0: return p.getIdPedido();
            case 1: return nomesProdutos(p.getProdutos());
            case 2: return p.getPrecoTotal();
            case 3: return p.getStatus_pedido().name();
            default: return p.getTipo_pagamento().name();
        }
    }
}