package dominio;

import java.util.List;

public class Livro extends EntidadeDominio{
    private String titulo;
    private int ano;
    private int edicao;
    private String isbn;
    private int numeroPaginas;
    private String sinopse;
    private String codigoBarras;
    private GrupoPrecificacao grupoPrecificacao;
    private int id;
    private boolean status;
    private List<Autor> autores; // Um livro pode ter vários autores
    private List<Categoria> categorias; // Um livro pode pertencer a várias categorias
    private Editora editora;
    private Dimensoes dimensoes;
    private String justificativaInativacao;
    private String justificativaAtivacao;
    private CategoriaInativacao categoriaInativacao;
    private CategoriaAtivacao categoriaAtivacao;
    private String caminhoImagem;


    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getCodigoBarras() {return codigoBarras;}

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public GrupoPrecificacao getGrupoPrecificacao() {
        return grupoPrecificacao;
    }

    public void setGrupoPrecificacao(GrupoPrecificacao grupoPrecificacao) {this.grupoPrecificacao = grupoPrecificacao;}

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public Dimensoes getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(Dimensoes dimensoes) {
        this.dimensoes = dimensoes;
    }

    public boolean isStatus() {return status;}

    public void setStatus(boolean status) {this.status = status;}

    public String getJustificativaInativacao() {
        return justificativaInativacao;
    }

    public void setJustificativaInativacao(String justificativaInativacao) {
        this.justificativaInativacao = justificativaInativacao;
    }

    public String getJustificativaAtivacao() {
        return justificativaAtivacao;
    }

    public void setJustificativaAtivacao(String justificativaAtivacao) {
        this.justificativaAtivacao = justificativaAtivacao;
    }

    public CategoriaInativacao getCategoriaInativacao() {
        return categoriaInativacao;
    }

    public void setCategoriaInativacao(CategoriaInativacao categoriaInativacao) {
        this.categoriaInativacao = categoriaInativacao;
    }

    public CategoriaAtivacao getCategoriaAtivacao() {
        return categoriaAtivacao;
    }

    public void setCategoriaAtivacao(CategoriaAtivacao categoriaAtivacao) {
        this.categoriaAtivacao = categoriaAtivacao;
    }
}
