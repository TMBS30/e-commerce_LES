package dominio;

public class Cupom {
    private int id;
    private TipoCupom tipoCupom;
    private double valor;
    private boolean usado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoCupom getTipoCupom() {
        return tipoCupom;
    }

    public void setTipoCupom(TipoCupom tipoCupom) {
        this.tipoCupom = tipoCupom;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }
}
