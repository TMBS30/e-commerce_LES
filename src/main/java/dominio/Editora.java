package dominio;

public class Editora extends EntidadeDominio {
    private String nome;
    private Telefone telefone;
    private int id;

    public Editora(int idEdit, String nomeEdit) {
        this.id = idEdit;
        this.nome = nomeEdit;
    }

    public Editora() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
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
