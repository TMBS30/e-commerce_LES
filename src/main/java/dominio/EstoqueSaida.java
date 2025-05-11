package dominio;

import java.time.LocalDate;

public class EstoqueSaida {
    private int idSaida;
    private int idLivro;
    private int quantidade;
    private LocalDate dataSaida;
    private TipoSaida motivoSaida;

    public int getIdSaida() {
        return idSaida;
    }

    public void setIdSaida(int idSaida) {
        this.idSaida = idSaida;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public TipoSaida getMotivoSaida() {
        return motivoSaida;
    }

    public void setMotivoSaida(TipoSaida motivoSaida) {
        this.motivoSaida = motivoSaida;
    }
}
