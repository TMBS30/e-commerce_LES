package dao;

import dominio.EntidadeDominio;

import java.sql.Connection;
import java.util.List;

public interface IDAO {
    public String salvar(EntidadeDominio entidade, Connection conn);
    public String alterar(EntidadeDominio entidade);
    public List<EntidadeDominio> consultar(EntidadeDominio entidade);
    public String excluir(EntidadeDominio entidade);
    public EntidadeDominio selecionar(EntidadeDominio entidade);
}
