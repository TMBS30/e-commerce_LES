package dominio;

public class Categoria extends EntidadeDominio{
    private String nome;
    private String descricao;
    private int id;

    public Categoria(int idCategoria, String nomeCategoria, String descricaoCategoria) {
        this.id = idCategoria;
        this.nome = nomeCategoria;
        this.descricao = descricaoCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
