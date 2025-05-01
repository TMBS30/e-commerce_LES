package dominio;

import java.util.List;

public class Troca {
    private List<Item> itens;
    private String dataTroca;
    private int id;

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataTroca() {
        return dataTroca;
    }

    public void setDataTroca(String dataTroca) {
        this.dataTroca = dataTroca;
    }
}
