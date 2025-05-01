package dominio;

public enum StatusCompra {
    EM_PROCESSAMENTO(1,"EM PROCESSAMENTO"),
    APROVADA(2,"APROVADA"),
    REPROVADA(3,"REPROVADA"),
    EM_TRANSPORTE(4,"EM TRANSPORTE"),
    ENTREGUE(5,"ENTREGUE"),
    EM_TROCA(6,"EM TROCA"),
    TROCA_AUTORIZADA(7,"TROCA AUTORIZADA");

    private int id;
    private String descricao;

    StatusCompra(int id, String descricao) {
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
