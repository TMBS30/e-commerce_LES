package dominio;

public class CarrinhoItem extends EntidadeDominio{
    private int idCarrinho;
    private int idLivro;
    private int quantidade;
    private int idItem;
    private Item item;
    private int id;



    public CarrinhoItem(int idCarrinho, int idLivro, int quantidade, Item item, int id) {
        this.idCarrinho = idCarrinho;
        this.idLivro = idLivro;
        this.quantidade = quantidade;
        this.item = item;
        this.id = id;

    }

    public CarrinhoItem() {

    }


    public int getIdCarrinho() { return idCarrinho; }
    public void setIdCarrinho(int idCarrinho) { this.idCarrinho = idCarrinho; }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdLivro() { return idLivro; }
    public void setIdLivro(int idLivro) { this.idLivro = idLivro; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    @Override
    public String toString() {
        return "CarrinhoItem{" +
                "id=" + id +
                "idCarrinho=" + idCarrinho +
                ", idLivro=" + idLivro +
                ", quantidade=" + quantidade +
                ", valorVenda=" + (item != null ? item.getValorVenda() : "N/A") +
                '}';
    }
}