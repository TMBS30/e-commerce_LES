package implementacao;

import dominio.EntidadeDominio;

public interface IStrategy {
    public String processar(EntidadeDominio entidade);
}
