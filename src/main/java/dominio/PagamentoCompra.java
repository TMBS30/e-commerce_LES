package dominio;

import java.security.Timestamp;

public class PagamentoCompra {
    private int idPagamentoCompra;
    private int idCompra;
    private Integer idCartao;
    private Integer idCupomUsado;
    private double valorPago;
    private double valorFrete;
    private String tipoPagamento;
    private Timestamp dataCriacao;
    private Timestamp dataModificacao;

    // Construtores
    public PagamentoCompra() {
    }

    public PagamentoCompra(int idCompra, Integer idCartao, Integer idCupomUsado, double valorPago, double valorFrete, String tipoPagamento) {
        this.idCompra = idCompra;
        this.idCartao = idCartao;
        this.idCupomUsado = idCupomUsado;
        this.valorPago = valorPago;
        this.valorFrete = valorFrete;
        this.tipoPagamento = tipoPagamento;
    }

    // Getters e Setters
    public int getIdPagamentoCompra() {
        return idPagamentoCompra;
    }

    public void setIdPagamentoCompra(int idPagamentoCompra) {
        this.idPagamentoCompra = idPagamentoCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(Integer idCartao) {
        this.idCartao = idCartao;
    }

    public Integer getIdCupomUsado() {
        return idCupomUsado;
    }

    public void setIdCupomUsado(Integer idCupomUsado) {
        this.idCupomUsado = idCupomUsado;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Timestamp getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Timestamp dataModificacao) {
        this.dataModificacao = dataModificacao;
    }
}
