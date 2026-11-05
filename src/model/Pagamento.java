package model;

public enum Pagamento {

    NA_ENTREGA("NA ENTREGA"),
    PIX("PIX"),
    CARTAO("CARTÃO");

    private final String descricao;

    Pagamento(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}