package dominio;

public enum TipoTelefone {
    CELULAR(1,"CELULAR"),
    RESIDENCIAL(2,"RESIDENCIAL"),
    COMERCIAL(3,"COMERCIAL"),
    OUTRO(4,"OUTRO");

    private int id;
    private String descricao;

    TipoTelefone(int id, String descricao) {
        this.id = id;
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

    public static TipoTelefone fromDescricao(String descricao) {
        for (TipoTelefone tipo : TipoTelefone.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo Telefone não encontrado para descrição: " + descricao);
    }
}
