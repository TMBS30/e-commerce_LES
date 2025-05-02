package dominio;


public enum BandeiraCartao {
    VISA(1,"VISA"),
    MASTERCARD(2,"MASTERCARD"),
    AMERICAN_EXPRESS(3,"AMERICAN_EXPRESS"),
    DISCOVER(4,"DISCOVER"),
    DINERS_CLUB(5,"DINERS_CLUB"),
    JCB(6,"JCB"),
    Outras(7,"Outras");

    private int id;
    private String descricao;

    BandeiraCartao(int id, String descricao) {
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

    public static BandeiraCartao fromDescricao(String descricao) {
        for (BandeiraCartao bandeira : BandeiraCartao.values()) {
            if (bandeira.getDescricao().equalsIgnoreCase(descricao)) {
                return bandeira;
            }
        }
        return BandeiraCartao.Outras;
    }
}
