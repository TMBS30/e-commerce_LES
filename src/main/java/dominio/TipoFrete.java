package dominio;

public enum TipoFrete {
    EXPRESS("Express", 2, 15.00),
    PADRAO("Padrão", 5, 8.00),
    ECONOMICO("Econômico", 9, 4.00);

    private final String descricao;
    private final int prazoEntrega;
    private final double valor;

    TipoFrete(String descricao, int prazoEntrega, double valor) {
        this.descricao = descricao;
        this.prazoEntrega = prazoEntrega;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return descricao + " (" + prazoEntrega + " dias úteis) - R$" + String.format("%.2f", valor);
    }

    public static TipoFrete fromString(String text) {
        for (TipoFrete tipo : TipoFrete.values()) {
            if (tipo.descricao.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de frete inválido: " + text);
    }
}
