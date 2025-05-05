package dominio;

import java.security.Timestamp;

public class Item {
    private int id;
    private int quantidade;
    private double valorCusto;
    private java.sql.Timestamp dataEntrada;
    private Fornecedor fornecedor;
    private double valorVenda;
    private int livroId;
    private int idFornecedor;

    private Livro livro;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(double valorCusto) {
        this.valorCusto = valorCusto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public java.sql.Timestamp getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(java.sql.Timestamp dataEntrada) { // Alterado para java.sql.Timestamp
        this.dataEntrada = dataEntrada;
    }

    public int getLivroId() {
        return livroId;
    }

    public void setLivroId(int livroId) {
        this.livroId = livroId;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

}
