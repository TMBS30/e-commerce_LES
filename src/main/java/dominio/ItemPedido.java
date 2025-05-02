package dominio;

public class ItemPedido {
    private int idItemPedido;
    private Livro livro;
    private int quantidade;
    private double valorUnitario;
    private int idCompra;

    public ItemPedido(Livro livro, int quantidade, double valorUnitario) {
        this.livro = livro;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public ItemPedido() {

    }

    public int getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(int idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public Livro getLivro() {
        return livro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }
}
