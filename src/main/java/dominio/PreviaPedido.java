package dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PreviaPedido {
    private int idPreviaPedido;
    private int idCliente;
    private int idEnderecoEntrega;
    private int idFrete;
    private Integer idCupom; // opcional
    private BigDecimal valorSubtotal;
    private BigDecimal valorFrete;
    private BigDecimal valorDesconto;
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;

    public PreviaPedido(int idPreviaPedido, int idCliente, int idEnderecoEntrega, int idFrete, Integer idCupom,
                        BigDecimal valorSubtotal, BigDecimal valorFrete, BigDecimal valorDesconto,
                        BigDecimal valorTotal, LocalDateTime dataCriacao) {
        this.idPreviaPedido = idPreviaPedido;
        this.idCliente = idCliente;
        this.idEnderecoEntrega = idEnderecoEntrega;
        this.idFrete = idFrete;
        this.idCupom = idCupom;
        this.valorSubtotal = valorSubtotal;
        this.valorFrete = valorFrete;
        this.valorDesconto = valorDesconto;
        this.valorTotal = valorTotal;
        this.dataCriacao = dataCriacao;
    }

    public int getIdPreviaPedido() { return idPreviaPedido; }
    public void setIdPreviaPedido(int idPreviaPedido) { this.idPreviaPedido = idPreviaPedido; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdEnderecoEntrega() { return idEnderecoEntrega; }
    public void setIdEnderecoEntrega(int idEnderecoEntrega) { this.idEnderecoEntrega = idEnderecoEntrega; }

    public int getIdFrete() { return idFrete; }
    public void setIdFrete(int idFrete) { this.idFrete = idFrete; }

    public Integer getIdCupom() { return idCupom; }
    public void setIdCupom(Integer idCupom) { this.idCupom = idCupom; }

    public BigDecimal getValorSubtotal() { return valorSubtotal; }
    public void setValorSubtotal(BigDecimal valorSubtotal) { this.valorSubtotal = valorSubtotal; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    public BigDecimal getValorDesconto() { return valorDesconto; }
    public void setValorDesconto(BigDecimal valorDesconto) { this.valorDesconto = valorDesconto; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
