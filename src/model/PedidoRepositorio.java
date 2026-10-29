package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Pedido;
import model.Produto;
import model.Status;


/** Repositorio em memoria. Mesmos metodos publicos do PedidoRepositorioBD. */
public class PedidoRepositorio {

    private List<Pedido> pedidos = new ArrayList<>();
    private int proximoId = 1;

    // 1. as regras do objeto sozinho, ja escritas no Model
    public void salvar(Pedido ped) {
        String problema = ped.validar();

        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }

        // 2. so agora grava: valide antes de alterar o estado.
        //    O id novo e gerado aqui, como o AUTO_INCREMENT faria no banco.
        ped.setIdPedido(proximoId++);
        pedidos.add(ped);
    }

    public void alterar(Pedido ped) {
        // regra que depende dos OUTROS registros: finalizado nao muda
        if (existe(ped.getIdPedido())) {
            throw new IllegalArgumentException("Não é possível alterar um pedido finalizado.");
        }
        String problema = ped.validar();
        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }
        // procura na lista o pedido com o mesmo id e troca pelo atualizado
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getIdPedido() == ped.getIdPedido()) {
                pedidos.set(i, ped);
                return;
            }
        }
        throw new IllegalArgumentException("Pedido não encontrado.");
    }

    public void excluir(int idPedido) {
        if (existe(idPedido)) {
            throw new IllegalArgumentException("Não é possível excluir um pedido finalizado.");
        }
        boolean removeu = pedidos.removeIf(p -> p.getIdPedido() == idPedido);
        if (!removeu) {
            throw new IllegalArgumentException("Pedido não encontrado.");
        }
    }

    // true se o pedido existe e esta com status FINALIZADO
    private boolean existe(int idPedido) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == idPedido && p.getStatus_pedido() == Status.FINALIZADO) {
                return true;
            }
        }
        return false;
    }

    /** status null = todos; trecho = parte do id ou do texto dos produtos. */
    public List<Pedido> listarFiltrado(Status status, String trecho) {
        String t = (trecho == null) ? "" : trecho.trim().toLowerCase();
        List<Pedido> r = new ArrayList<>();
        for (Pedido p : pedidos) {
            boolean statusOk = (status == null || p.getStatus_pedido() == status);
            boolean textoOk = String.valueOf(p.getIdPedido()).contains(t)
                    || p.getProdutos().toString().toLowerCase().contains(t);
            if (statusOk && textoOk) {
                r.add(p);
            }
        }
        return r;
    }

    public Map<String, Integer> contarPorStatus() {
        Map<String, Integer> m = new LinkedHashMap<>();
        for (Status s : Status.values()) {
            m.put(s.name(), 0);
        }
        for (Pedido p : pedidos) {
            m.merge(p.getStatus_pedido().name(), 1, Integer::sum);
        }
        return m;
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }

    public int contar() {
        return pedidos.size();
    }
}