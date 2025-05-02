package dominio;

public enum Genero {
    FEMININO(1,"FEMININO"),
    MASCULINO(2,"MASCULINO"),
    OUTRO(3,"OUTRO");

    private String descricao;
    private int id;

    Genero(int id, String descricao) {
        this.setId(id);
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Genero fromDescricao(String descricao) {
        for (Genero genero : Genero.values()) {
            if (genero.getDescricao().equalsIgnoreCase(descricao)) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Gênero inválido: " + descricao);
    }
}

