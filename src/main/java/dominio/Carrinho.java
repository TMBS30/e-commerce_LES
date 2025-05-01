package dominio;


import java.util.List;


public class Carrinho {
    private List<CarrinhoItem> itens;
    private boolean bloqueado;
    private String dataBloqueio;
    private double valorCarrinho;
    private int id;
    private int clienteId;


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
}
