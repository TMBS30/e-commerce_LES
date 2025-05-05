package dominio;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Carrinho {
    private List<CarrinhoItem> itens;
    private boolean bloqueado;
    private String dataBloqueio;
    private double valorCarrinho;
    private int id;
    private int clienteId;
    private Date ultimaAtividade;

    public Carrinho() {
        this.itens = new ArrayList<>();
        this.ultimaAtividade = new Date();
    }

    public List<CarrinhoItem> getItens() {
        return itens;
    }

    public void setItens(List<CarrinhoItem> itens) {
        this.itens = itens;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getDataBloqueio() {
        return dataBloqueio;
    }

    public void setDataBloqueio(String dataBloqueio) {
        this.dataBloqueio = dataBloqueio;
    }

    public double getValorCarrinho() {
        return valorCarrinho;
    }

    public void setValorCarrinho(double valorCarrinho) {
        this.valorCarrinho = valorCarrinho;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public Date getUltimaAtividade() {
        return ultimaAtividade;
    }

    public void setUltimaAtividade(Date ultimaAtividade) {
        this.ultimaAtividade = ultimaAtividade;
    }

    public void atualizarUltimaAtividade() {
        this.ultimaAtividade = new Date();
    }
}
