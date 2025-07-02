package dominio;

import java.time.LocalDate;
import java.util.Date;

public class CupomTroca {
    private int idCupomTroca;
    private String codigoCupom;
    private double valorCupom;
    private int idCliente;
    private int idCompra;
    private LocalDate dataCriacao;
    private boolean usado;

    public CupomTroca() {
    }

    public CupomTroca(String codigoCupom, double valorCupom, int idCliente, int idCompra) {
        this.codigoCupom = codigoCupom;
        this.valorCupom = valorCupom;
        this.idCliente = idCliente;
        this.idCompra = idCompra;
    }

    // Getters e Setters
    public int getIdCupomTroca() {
        return idCupomTroca;
    }

    public void setIdCupomTroca(int idCupomTroca) {
        this.idCupomTroca = idCupomTroca;
    }

    public String getCodigoCupom() {
        return codigoCupom;
    }

    public void setCodigoCupom(String codigoCupom) {
        this.codigoCupom = codigoCupom;
    }

    public double getValorCupom() {
        return valorCupom;
    }

    public void setValorCupom(double valorCupom) {
        this.valorCupom = valorCupom;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
