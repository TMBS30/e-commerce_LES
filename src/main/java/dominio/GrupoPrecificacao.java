package dominio;

public class GrupoPrecificacao {
    private int id;
    private String nome;
    private double margemLucroMinima;
    private double descontoMaximo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getMargemLucroMinima() {
        return margemLucroMinima;
    }

    public void setMargemLucroMinima(double margemLucroMinima) {
        this.margemLucroMinima = margemLucroMinima;
    }

    public double getDescontoMaximo() {
        return descontoMaximo;
    }

    public void setDescontoMaximo(double descontoMaximo) {
        this.descontoMaximo = descontoMaximo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
