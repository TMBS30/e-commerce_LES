package dominio;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Estoque {
    private int idEstoque;
    private int idLivro;
    private TipoEntrada tipoEntrada; // Usando um Enum para os tipos de entrada
    private int quantidadeEstoque;
    private Integer idFornecedor; // Pode ser nulo
    private BigDecimal valorCusto; // Pode ser nulo
    private LocalDate dataEntrada;

    // Construtores
    public Estoque() {
        this.dataEntrada = LocalDate.now(); // Define a data atual por padrão
        this.tipoEntrada = TipoEntrada.COMPRA; // Define o tipo padrão
    }

    public Estoque(int idLivro, int quantidadeEstoque) {
        this(); // Chama o construtor padrão para inicializar dataEntrada e tipoEntrada
        this.idLivro = idLivro;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters e Setters para todos os atributos
    public int getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(int idEstoque) {
        this.idEstoque = idEstoque;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    @Override
    public String toString() {
        return "Estoque{" +
                "idEstoque=" + idEstoque +
                ", idLivro=" + idLivro +
                ", tipoEntrada=" + tipoEntrada +
                ", quantidadeEstoque=" + quantidadeEstoque +
                ", idFornecedor=" + idFornecedor +
                ", valorCusto=" + valorCusto +
                ", dataEntrada=" + dataEntrada +
                '}';
    }
}