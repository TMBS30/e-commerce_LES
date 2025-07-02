package dominio;

public class Dimensoes {
    private double altura;
    private double largura;
    private double profundidade;
    private double peso;
    private int id;

    public Dimensoes(int id, double altura, double largura, double profundidade, double peso) {
        this.altura = altura;
        this.largura = largura;
        this.profundidade = profundidade;
        this.peso = peso;
        this.id = id;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getLargura() {
        return largura;
    }

    public void setLargura(double largura) {
        this.largura = largura;
    }

    public double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(double profundidade) {
        this.profundidade = profundidade;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

