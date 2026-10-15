
package model;

import java.util.List;
import java.util.ArrayList;

public class PedidoRepositorio {

    private List<Pedido> pedidos = new ArrayList<>();

 // 1. as regras do objeto sozinho, ja escritas no Model
    public void salvar(Pedido ped) {
        String problema = ped.validar();

        if (problema != null) {
            throw new IllegalArgumentException(problema);
        }
     // 2. a regra que depende dos OUTROS registros
        //  verifica se ja existe um pedido com esse id que tem status = finalizado
        if (existe(ped.getIdPedido())) {
            throw new IllegalArgumentException("Este pedido já foi finalizado.");
        }

        // 3. so agora grava: valide antes de alterar o estado
        	pedidos.add(ped);
    	}
    public void alterar(Pedido ped) {
        if (existe(ped.getIdPedido())) { //if (existe(ped.getIdPedido(), ped.getStatus())) {
            throw new IllegalArgumentException(
                "Este pedido já foi finalizado."
            );
        }
        String problema = ped.validar();	
        	if (problema != null) {
        		throw new IllegalArgumentException(problema);
     }
        	// verifica se o iddo pedido que esta na lista é = id do pedido que vai ser atualizaso
        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getIdPedido() == ped.getIdPedido()) {
                pedidos.set(i, ped);
                return;
            }
        }
        		throw new IllegalArgumentException("Pedido não encontrado." );
    }

    	// se o pedido tiver status pedido = finalizado = true
    private boolean existe(int idPedido) {
        for (Pedido p : pedidos) {
            if (p.getIdPedido() == idPedido && p.getStatus_pedido() == Status.FINALIZADO) {
                return true;
            }
        }
        return false;
    }

    public List<Pedido> listarTodos() {
        return new ArrayList<>(pedidos);
    }

    public int contar() {
        return pedidos.size();
    }
}