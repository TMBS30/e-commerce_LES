package dominio;

public enum TipoPagamento {
    CARTAO (1,"CARTAO"),
    CUPOM (2,"CUPOM");

    private int id;
    private String descricao;

    TipoPagamento (int id, String descricao) {
        this.setId(id);
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao=descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
