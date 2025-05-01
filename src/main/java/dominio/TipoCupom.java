package dominio;

public enum TipoCupom {
    PROMOCIONAL (1,"PROMOCIONAL"),
    TROCA (2,"TROCA");

    private int id;
    private String descricao;

    TipoCupom(int id, String descricao) {
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

    public static TipoCupom fromDescricao(String descricao) {
        for (TipoCupom tipo : TipoCupom.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}
