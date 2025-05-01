package dominio;

public enum TipoEndereco {
    RESIDENCIAL(1, "Residencial"),
    COBRANCA(2, "Cobranca"),
    ENTREGA(3, "Entrega");

    private String descricao;
    private int id;

    // Adicionando id ao construtor
    TipoEndereco(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static TipoEndereco fromDescricao(String descricao) {
        for (TipoEndereco tipo : TipoEndereco.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("TipoEndereco não encontrado para descrição: " + descricao);
    }
}