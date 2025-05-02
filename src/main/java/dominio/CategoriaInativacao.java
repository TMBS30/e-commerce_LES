package dominio;

public enum CategoriaInativacao {
    ESGOTADO (1,"ESGOTADO"),
    CONTEUDO_DESATUALIZADO(2,"CONTEUDO DESATUALIZADO"),
    BAIXA_PROCURA(3,"BAIXA PROCURA"),
    OUTROS(4,"OUTROS");

    private int id;
    private String descricao;

    CategoriaInativacao(int id, String descricao) {
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
