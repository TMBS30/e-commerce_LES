package dominio;

public enum CategoriaAtivacao {
    DISPONIVEL (1,"DISPONIVEL"),
    NOVA_EDICAO(2,"NOVA EDICAO"),
    ALTA_PROCURA(3,"ALTA PROCURA"),
    OUTROS(4,"OUTROS");

    private int id;
    private String descricao;

    CategoriaAtivacao(int id, String descricao) {
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
