package dominio;

import java.security.Timestamp;

public class ItemCompra {
    private int idItemCompra;
    private int idCompra;
    private int idLivro;
    private int quantidade;
    private double valorUnitarioNaCompra;
    private Timestamp dataCriacao;
    private Timestamp dataModificacao;
    private Livro livro;

    // Construtores
    public ItemCompra() {
    }

    public ItemCompra(int idCompra, int idLivro, int quantidade, double valorUnitarioNaCompra) {
        this.idCompra = idCompra;
        this.idLivro = idLivro;
        this.quantidade = quantidade;
        this.valorUnitarioNaCompra = valorUnitarioNaCompra;
    }

    // Getters e Setters
    public int getIdItemCompra() {
        return idItemCompra;
    }

    public void setIdItemCompra(int idItemCompra) {
        this.idItemCompra = idItemCompra;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitarioNaCompra() {
        return valorUnitarioNaCompra;
    }

    public void setValorUnitarioNaCompra(double valorUnitarioNaCompra) {
        this.valorUnitarioNaCompra = valorUnitarioNaCompra;
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

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }
}
