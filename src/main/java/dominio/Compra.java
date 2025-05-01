package dominio;

import java.security.Timestamp;
import java.util.List;

public class Compra {
    private int idCompra;
    private int clienteId;
    private int idEnderecoEntrega;
    private int idEnderecoCobranca;
    private double valorSubtotal;
    private TipoFrete valorFrete;
    private double valorDescontoTotal;
    private double valorTotalCompra;
    private Timestamp dataHoraCompra;
    private int statusIdCompra;
    private String observacoes;
    private java.sql.Timestamp dataCriacao;
    private java.sql.Timestamp dataModificacao;
    private String numeroPedido;
    private List<ItemPedido> itensPedido;
    private List<ItemCompra> itensCompra;
    private boolean troca;
    private String statusDescricao;

    // Construtores
    public Compra() {
    }

    public Compra(int clienteId, int idEnderecoEntrega, int idEnderecoCobranca, double valorSubtotal, TipoFrete valorFrete, double valorDescontoTotal, double valorTotalCompra, int statusIdCompra, String numeroPedido/*,boolean troca*/) {
        this.clienteId = clienteId;
        this.idEnderecoEntrega = idEnderecoEntrega;
        this.idEnderecoCobranca = idEnderecoCobranca;
        this.valorSubtotal = valorSubtotal;
        this.valorFrete = valorFrete;
        this.valorDescontoTotal = valorDescontoTotal;
        this.valorTotalCompra = valorTotalCompra;
        this.statusIdCompra = statusIdCompra;
        this.numeroPedido = numeroPedido;
        //this.troca = troca;

    }

    // Getters e Setters
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getIdEnderecoEntrega() {
        return idEnderecoEntrega;
    }

    public void setIdEnderecoEntrega(int idEnderecoEntrega) {
        this.idEnderecoEntrega = idEnderecoEntrega;
    }

    public int getIdEnderecoCobranca() {
        return idEnderecoCobranca;
    }

    public void setIdEnderecoCobranca(int idEnderecoCobranca) {
        this.idEnderecoCobranca = idEnderecoCobranca;
    }

    public double getValorSubtotal() {
        return valorSubtotal;
    }

    public void setValorSubtotal(double valorSubtotal) {
        this.valorSubtotal = valorSubtotal;
    }

    public TipoFrete getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(TipoFrete valorFrete) {
        this.valorFrete = valorFrete;
    }

    public double getValorDescontoTotal() {
        return valorDescontoTotal;
    }

    public void setValorDescontoTotal(double valorDescontoTotal) {
        this.valorDescontoTotal = valorDescontoTotal;
    }

    public double getValorTotalCompra() {
        return valorTotalCompra;
    }

    public void setValorTotalCompra(double valorTotalCompra) {
        this.valorTotalCompra = valorTotalCompra;
    }

    public Timestamp getDataHoraCompra() {
        return dataHoraCompra;
    }

    public void setDataHoraCompra(Timestamp dataHoraCompra) {
        this.dataHoraCompra = dataHoraCompra;
    }

    public int getStatusIdCompra() {
        return statusIdCompra;
    }

    public void setStatusIdCompra(int statusIdCompra) {
        this.statusIdCompra = statusIdCompra;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public java.sql.Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(java.sql.Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public java.sql.Timestamp getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(java.sql.Timestamp dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }
    
    public void setItensPedido(List<ItemPedido> itensExibicao) {
        this.itensPedido= itensExibicao;
    }

    public void setDataHoraCompra(java.sql.Timestamp dataHoraCompra) {
    }

    public boolean isTroca() {
        return troca;
    }

    public void setTroca(boolean troca) {
        this.troca = troca;
    }

    public String getStatusDescricao() {
        return statusDescricao;
    }

    public void setStatusDescricao(String statusDescricao) {
        this.statusDescricao = statusDescricao;
    }

    public List<ItemCompra> getItensCompra() {
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompra> itensCompra) {
        this.itensCompra = itensCompra;
    }
}
