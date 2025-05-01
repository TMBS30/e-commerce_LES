package dominio;

public enum Fornecedor {
    INGRAM_CONTENT_GROUP(1,"INGRAM CONTENT GROUP"),
    AMAZON_KDP_E_DISTRIBUICAO(2,"AMAZON KDP E DISTRIBUICAO"),
    SARAIVA_DISTRIBUIDORA(3,"SARAIVA DISTRIBUIDORA"),
    BOOKWIRE_BRASIL(4,"BOOKWIRE BRASIL"),
    CATAVENTO_DISTRIBUIDORA(5,"CATAVENTO DISTRIBUIDORA"),
    OUTROS(6,"OUTROS");

    private int id;
    private String descricao;

    Fornecedor(int id, String descricao) {
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
