package dominio;

import java.math.BigDecimal;

public class PreviaPedidoItem {
    private int idPreviaPedido;
    private int idLivro;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public PreviaPedidoItem(int idPreviaPedido, int idLivro, int quantidade,
                            BigDecimal valorUnitario, BigDecimal valorTotal) {
        this.idPreviaPedido = idPreviaPedido;
        this.idLivro = idLivro;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    public int getIdPreviaPedido() { return idPreviaPedido; }
    public void setIdPreviaPedido(int idPreviaPedido) { this.idPreviaPedido = idPreviaPedido; }

    public int getIdLivro() { return idLivro; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}
