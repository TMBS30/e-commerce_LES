package dominio;

public class Telefone extends EntidadeDominio{
    private String ddd;
    private String numero;
    private TipoTelefone tipo;
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public TipoTelefone getTipo() {
        return tipo;
    }
    public void setTipo(TipoTelefone tipo) {
        this.tipo = tipo;
    }
    public String getDdd() {
        return ddd;
    }
    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
}