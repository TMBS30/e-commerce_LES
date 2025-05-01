package dominio;

public enum TipoLogradouro {
    RUA(1,"Rua"),
    AVENIDA(2,"Avenida"),
    TRAVESSA(3,"Travessa"),
    ALAMEDA(4,"Alameda"),
    ESTRADA(5,"Estrada"),
    OUTRO(6,"Outro");

    private String descricao;
    private int id;

    TipoLogradouro(int id, String descricao) {
        this.setId(id);
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao=descricao;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }
    public static TipoLogradouro fromDescricao(String descricao) {
        for (TipoLogradouro tipo : TipoLogradouro.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo Logradouro não encontrado para descrição: " + descricao);
    }
}

