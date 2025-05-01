package dominio;

public class Cidade extends EntidadeDominio{

    private int id;
    private String nomeCidade;
    private Estado estado;


    public String getNome() {
        return nomeCidade;
    }

    public void setNome(String nomeCidade) {
        this.nomeCidade=nomeCidade;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}

